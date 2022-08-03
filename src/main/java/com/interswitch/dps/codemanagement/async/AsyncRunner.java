package com.interswitch.dps.codemanagement.async;

import com.interswitch.dps.codemanagement.enums.CodeGenerationTypes;
import com.interswitch.dps.codemanagement.enums.CodeProcessStatus;
import com.interswitch.dps.codemanagement.models.*;
import com.interswitch.dps.codemanagement.repositories.ClientHashedCodeRepository;
import com.interswitch.dps.codemanagement.repositories.CodeManagementRepository;
import com.interswitch.dps.codemanagement.repositories.GeneratedCodeFileRepository;
import com.interswitch.dps.codemanagement.services.interfaces.ClientConfigService;
import com.interswitch.dps.codemanagement.utils.HashAlgorithmnUtil;
import com.interswitch.dps.codemanagement.utils.Validators;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class AsyncRunner {
    @Autowired
    private ClientConfigService clientConfigService;
    @Autowired
    private CodeManagementRepository codeManagementRepository;
    @Autowired
    private GeneratedCodeFileRepository generatedCodeFileRepository;
    @Value("${app.docs.base-uri}")
    private String baseUri;
    @Value("${app-docs.code-dir}")
    private String codeDir;
    @Autowired
    private ClientHashedCodeRepository clientHashedCodeRepository;

    @Async
    public void generateClientCodes(CodeGenerationRequest request) {
        log.info("Client Code Generation happening on a thread 1:==>" + Thread.currentThread().getName());

        request.setStatus(CodeProcessStatus.PROCESSING.name());
        codeManagementRepository.save(request);

        ClientConfig config = clientConfigService.getClientConfig(request.getClientId());
        HashAlgorithmnUtil hashAlgorithmnUtil = new HashAlgorithmnUtil(config.getAlgorithmKey());
        Map<String, Set<String>> result = generateCodes(request, hashAlgorithmnUtil);
        Set<String> plainCodes = result.get("plainCodes");
        Set<String> hashedCodes = result.get("hashedCodes");

        //Write to files
        Map<String, List<GeneratedFile>> fileMap = writeCodestoCSV(request.getCodePerFile(), plainCodes, hashedCodes, config.getClientName());

        request.setStatus(CodeProcessStatus.PROCESSED.name());
        codeManagementRepository.save(request);

        GeneratedCodeFile generatedCodeFile = new GeneratedCodeFile();
        generatedCodeFile.setCodeGenerationRequestId(request.getId());
        generatedCodeFile.setPlainCodeFiles(fileMap.get("plainGeneratedFiles"));
        generatedCodeFile.setHashedCodeFiles(fileMap.get("hashedGeneratedFiles"));
        generatedCodeFile.setIsActivated(false);

        generatedCodeFileRepository.insert(generatedCodeFile);

        log.info("process completed!");
    }

    public Map<String, Set<String>> generateCodes(CodeGenerationRequest request, HashAlgorithmnUtil hashAlgorithmnUtil) {
        Map<String, Set<String>> codeMap = new HashMap<>();
        Set<String> plainCodes = new HashSet<>();
        Set<String> hashedCodes = new HashSet<>();

        log.info("Generation starts - " + new Date());
        for (int i = 0; i < request.getNumberOfCodes(); i++) {
            String codeValue = "";
            if(request.getCodeType().equalsIgnoreCase(CodeGenerationTypes.NUMERIC.name())){
                codeValue = RandomStringUtils.randomNumeric(request.getCodeLength());
            }else if(request.getCodeType().equalsIgnoreCase(CodeGenerationTypes.ALPHABETICAL.name())) {
                codeValue = RandomStringUtils.randomAlphabetic(request.getCodeLength());
            }else {
                codeValue = RandomStringUtils.randomAlphanumeric(request.getCodeLength());
            }

            if(!Validators.isNullOrEmptyString(request.getPrefixValue())) {
                codeValue = request.getPrefixValue().concat(codeValue);
            }

            plainCodes.add(codeValue);
            hashedCodes.add(hashAlgorithmnUtil.hmac256(codeValue));

        }

        log.info("Generation ends - " + new Date());
        codeMap.put("plainCodes", plainCodes);
        codeMap.put("hashedCodes", hashedCodes);

        return codeMap;
    }

    private Map<String, List<GeneratedFile>> writeCodestoCSV(int codePerfile, Set<String> codes, Set<String> hashedCodes, String clientName) {
       try{
           int count = 1, codeCount = 0;
           int noOfCodePerFile = codePerfile;
           long time = new Date().getTime();
           List<GeneratedFile> plainGeneratedFiles = new ArrayList<>();
           List<GeneratedFile> hashedGeneratedFiles = new ArrayList<>();

           String plainCodeArr[] = new String[codes.size()];
           plainCodeArr = codes.toArray(plainCodeArr);

           String hashedCodeArr[] = new String[hashedCodes.size()];
           hashedCodeArr = hashedCodes.toArray(hashedCodeArr);

           String[] paths = constructFilePaths(clientName);
           String plainPath = paths[0];
           String hashedPath = paths[1];

           String plainFileName = time + "_plain_"+count+".csv";
           String hashedFileName = time + "_hashed_"+count+".csv";


           FileWriter plainfileWr = new FileWriter(plainPath + plainFileName);
           PrintWriter plainFilePr = new PrintWriter(plainfileWr);
           FileWriter hashedfileWr = new FileWriter(hashedPath + hashedFileName);
           PrintWriter hashedFilePr = new PrintWriter(hashedfileWr);

           plainGeneratedFiles.add(new GeneratedFile(plainFileName, plainPath + plainFileName));
           hashedGeneratedFiles.add(new GeneratedFile(hashedFileName, hashedPath + hashedFileName));

           log.info("BEGIN WRITING TO A FILE");
           for(int i = 0; i < plainCodeArr.length; i++) {
               if(codeCount == codePerfile) {
                   plainFilePr.close();
                   hashedFilePr.close();
                   count++;

                   plainFileName = time + "_plain_"+count+".csv";
                   plainfileWr = new FileWriter(plainPath + plainFileName);
                   plainFilePr = new PrintWriter(plainfileWr);

                   hashedFileName = time + "_hashed_"+count+".csv";
                   hashedfileWr = new FileWriter(hashedPath + hashedFileName);
                   hashedFilePr = new PrintWriter(hashedfileWr);

                   plainGeneratedFiles.add(new GeneratedFile(plainFileName, plainPath + plainFileName));
                   hashedGeneratedFiles.add(new GeneratedFile(hashedFileName, hashedPath + hashedFileName));

                   codeCount = 0;
               }

               plainFilePr.println(plainCodeArr[i]);
               hashedFilePr.println(hashedCodeArr[i]);
               codeCount++;
           }
           plainFilePr.close();
           hashedFilePr.close();

           Map<String, List<GeneratedFile>> fileMap = new HashMap<>();
           fileMap.put("plainGeneratedFiles", plainGeneratedFiles);
           fileMap.put("hashedGeneratedFiles", hashedGeneratedFiles);

           return fileMap;
        }
        catch(IOException exe){
            System.out.println("Cannot create file");
        }

      return new HashMap<>();
    }

    public String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    private String[] constructFilePaths(String clientName) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date currentDate = new Date();

        String plainPath = baseUri + codeDir + clientName +"/" +formatter.format(currentDate) + "/plain_codes/";
        String hashedPath = baseUri + codeDir + clientName +"/" +formatter.format(currentDate) + "/hashed_codes/";

        //create path if not exist
        createPath(plainPath);
        createPath(hashedPath);

        return new String[] {plainPath, hashedPath};
    }

    public void createPath(String path) {
        File dir = new File (path);
        if(!dir.exists())
            dir.mkdirs();
    }

    @Async
    public void activateCodes(GeneratedCodeFile generatedCodeFile) throws Exception {
        List<GeneratedFile> filePaths = generatedCodeFile.getHashedCodeFiles();
        for(GeneratedFile f: filePaths) {
            File file = new File(f.getFilepath());
            readFileAndSaveToDb(file, generatedCodeFile.getId());
        }

        generatedCodeFile.setIsActivated(true);
        generatedCodeFileRepository.save(generatedCodeFile);
        log.info("Code activated successfully");
    }

    private void readFileAndSaveToDb(File file, String generatedCodeFileId) throws Exception {
        InputStream stream = new FileInputStream(file);
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            String data;
            while((data = reader.readLine()) != null) {
                //TODO: correct to get the first index
                ClientHashedCode clientHashedCode = new ClientHashedCode();
                clientHashedCode.setGeneratedCodeFileId(generatedCodeFileId);
                clientHashedCode.setHashedCode(data);
                clientHashedCode.setIsUsed(false);

                clientHashedCodeRepository.insert(clientHashedCode);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
