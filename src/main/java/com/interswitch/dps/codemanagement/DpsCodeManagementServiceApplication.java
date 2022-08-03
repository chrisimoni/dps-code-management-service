package com.interswitch.dps.codemanagement;

import com.interswitch.dps.codemanagement.models.ClientConfig;
import com.interswitch.dps.codemanagement.models.CodeGenerationRequest;
import com.interswitch.dps.codemanagement.repositories.ClientConfigRepository;
import com.interswitch.dps.codemanagement.services.interfaces.CodeManagementService;
//import com.interswitch.dps.codemanagement.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@EnableAsync
@Slf4j
@ComponentScan("com.interswitch.dps.codemanagement.*")
@SpringBootApplication(exclude = {
		MongoAutoConfiguration.class,
		MongoDataAutoConfiguration.class
})

public class DpsCodeManagementServiceApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DpsCodeManagementServiceApplication.class, args);
	}



//
//	@Autowired
//	private JwtUtil jwtUtil;


	@Override
	public void run(String... args) throws Exception {

	log.info("DPS-CODE-MANAGEMENT-SERVICE started running...");
		log.info("Code Generation happening on a thread x:==>" + Thread.currentThread().getName());
		//
//		String user = jwtUtil.getUseFullNameFromToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJwZXJtaXNzaW9uIjpbIklzd0NyZWF0ZUFkbWluVXNlciIsIklzd1ZpZXdBZG1pblVzZXJzIiwiSXN3Vmlld0FkbWluVXNlciIsIklzd0VkaXRBZG1pblVzZXIiLCJJc3dVcGRhdGVBZG1pblVzZXJTdGF0dXMiLCJJc3dBcHByb3ZlT3JSZWplY3RBZG1pblVzZXIiLCJJc3dDcmVhdGVBZG1pblJvbGUiLCJJc3dFZGl0QWRtaW5Sb2xlIiwiSXN3Vmlld0FkbWluUm9sZSIsIklzd1ZpZXdBZG1pblJvbGVzIiwiSXN3Vmlld0FkbWluUGVybWlzc2lvbiIsIklzd1ZpZXdBZG1pblBlcm1pc3Npb25zIiwiSXN3VXBkYXRlQWRtaW5Sb2xlU3RhdHVzIiwiSXN3VXBkYXRlUGVybWlzc2lvblN0YXR1cyIsIklzd1VwZGF0ZUFkbWluUm9sZVBlcm1pc3Npb25zIiwiSXN3Vmlld0FkbWluTW9kdWxlcyIsIklzd1ZpZXdBZG1pbk1vZHVsZSIsIklzd1ZpZXdBZG1pbk1vZHVsZVBlcm1pc3Npb25zIiwiSXN3Vmlld0FkbWluTW9kdWxlUGVybWlzc2lvbiIsIklzd1ZpZXdBZG1pblJvbGVVc2VycyIsIklzd0RlbGV0ZUFkbWluUm9sZSIsIklzd0NyZWF0ZUNvcnBvcmF0ZSIsIklzd0VkaXRDb3Jwb3JhdGUiLCJJc3dWaWV3Q29ycG9yYXRlIiwiSXN3Vmlld0NvcnBvcmF0ZXMiLCJJc3dWaWV3VXNlcnMiLCJJc3dWaWV3VXNlciIsIklzd0VkaXRVc2VyIiwiSXN3VXBkYXRlVXNlclN0YXR1cyIsIklzd0FwcHJvdmVPclJlamVjdFVzZXIiLCJJc3dWaWV3UGVybWlzc2lvbnMiLCJJc3dWaWV3UGVybWlzc2lvbiIsIklzd0NyZWF0ZVJvbGUiLCJJc3dFZGl0Um9sZSIsIklzd1ZpZXdSb2xlIiwiSXN3Vmlld1JvbGVzIiwiSXN3VXBkYXRlUm9sZVBlcm1pc3Npb25zIiwiSXN3Vmlld01vZHVsZXMiLCJJc3dWaWV3TW9kdWxlIiwiSXN3Vmlld01vZHVsZVBlcm1pc3Npb25zIiwiSXN3Vmlld01vZHVsZVBlcm1pc3Npb24iLCJJc3dWaWV3Um9sZVVzZXJzIiwiSXN3RGVsZXRlUm9sZSIsIklzd0RlQWN0aXZhdGVVc2VyIiwiQ3JlYXRlVXNlciIsIlZpZXdVc2VyIiwiRWRpdFVzZXIiLCJWaWV3VXNlcnMiLCJWaWV3VXNlcnMiLCJVcGRhdGVVc2VyU3RhdHVzIiwiQXBwcm92ZU9yUmVqZWN0VXNlciIsIkRlQWN0aXZhdGVVc2VyIiwiQ3JlYXRlUm9sZSIsIkVkaXRSb2xlIiwiRGVsZXRlUm9sZSIsIlZpZXdSb2xlIiwiVmlld1JvbGVzIiwiVmlld1Blcm1pc3Npb25zIiwiVmlld1Blcm1pc3Npb24iLCJVcGRhdGVSb2xlU3RhdHVzIiwiVXBkYXRlUm9sZVBlcm1pc3Npb25zIiwiR2VuZXJhdGVDb2RlIiwiQmxhY2tsaXN0VXNlcnMiLCJCbGFja2xpc3RVc2VyIiwiQ3JlYXRlQ2FtcGFpZ25Db25maWd1cmF0aW9uIiwiRWRpdENhbXBhaWduQ29uZmlndXJhdGlvbiIsIkRlbGV0ZUNhbXBhaWduQ29uZmlndXJhdGlvbiIsIkNyZWF0ZUNhbXBhaWduIiwiRWRpdENhbXBhaWduIiwiVmlld0NhbXBhaWduIiwiVmlld0NhbXBhaWducyIsIlN0YXJ0Q2FtcGFpZ24iLCJQYXVzZUNhbXBhaWduIiwiQ3JlYXRlQ2FtcGFpZ25SZXdhcmQiLCJFZGl0Q2FtcGFpZ25SZXdhcmQiLCJDcmVhdGVSYWZmbGVSZXdhcmQiLCJFZGl0UmFmZmxlUmV3YXJkIiwiRGVsZXRlQ2FtcGFpZ25SZXdhcmQiLCJEZWxldGVSYWZmbGVSZXdhcmQiXSwibW9kdWxlIjpbIklTVyBBZG1pbiBVc2VyIE1hbmFnZW1lbnQgTW9kdWxlIiwiQ29ycG9yYXRlIFVzZXIgTWFuYWdlbWVudCBNb2R1bGUiLCJJU1cgUm9sZS9QZXJtaXNzaW9uIE1hbmFnZW1lbnQgTW9kdWxlIiwiQ29ycG9yYXRlIFJvbGUvUGVybWlzc2lvbiBNYW5hZ2VtZW50IE1vZHVsZSIsIkNvcnBvcmF0ZSBQb2ludCBCYXNlZCBJbnRlcmFjdGlvbiBNb2R1bGUiLCJDb3Jwb3JhdGUgUmV3YXJkIENvZGUvVG9rZW4gSW50ZXJhY3Rpb24gTW9kdWxlIiwiQ29ycG9yYXRlIFRyYW5zYWN0aW9uIEJhc2VkIE1vZHVsZSIsIkNvcnBvcmF0ZSBHaWZ0aW5nIE1vZHVsZSIsIkdpZnRpbmcgTW9kdWxlIiwiVXNlciBNYW5hZ2VtZW50IE1vZHVsZSIsIlJvbGUvUGVybWlzc2lvbiBNYW5hZ2VtZW50IE1vZHVsZSIsIlJld2FyZCBDb2RlL1Rva2VuIEludGVyYWN0aW9uIE1vZHVsZSIsIlBvaW50IEJhc2VkIEludGVyYWN0aW9uIE1vZHVsZSJdLCJ1c2VyX2lkIjoiNiIsIm5hbWUiOiJPbHV3YWtlbWkgQXdvc2lsZSIsImVtYWlsIjoia2VtaWppYmJvbGFAZ21haWwuY29tIiwiY29ycG9yYXRlX2lkIjoiMCIsIm5iZiI6MTYzMTA4OTIzNywiZXhwIjoxNjQxNjg1NjM5LCJpYXQiOjE2MzEwODkyMzd9.fj65Vet8QxQgX10TgHaCwLhRW3iy_lIEvCIVmsBSTDxnfvadz8TiSJQs99eCnP3HGipj7oXcUdAOACnyMEDSZKV7Bmg_75scCb_c_r_LAjvAowAFiKOM7h-FuCaK6BP_ZLYoDL5AKwtkh1OoKlCDgF0h3RKt15ANOCRYwUPiOF7F-6ewe_JomIxeNqQpAR5bgxD3J6k4Ry8qaYJVv1xfJl9WutY7rz5SOvjMLCX3OPN6_RG_JzG4MuNuU6TUJWs-lpxhIbWSUvmiEeIhY_lGnlJ0r7u012ymZfi_cFuM0NrsZBbejXGZ_WNA3-bbKzWGoYYoCRCeiXJu8xZDh2qZTA");
//		log.info("Name===>"+user);
//
//		log.info("+++---->");
//
//		log.info("Permissions ===>"+jwtUtil.getPermissionsFromToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJwZXJtaXNzaW9uIjpbIklzd0NyZWF0ZUFkbWluVXNlciIsIklzd1ZpZXdBZG1pblVzZXJzIiwiSXN3Vmlld0FkbWluVXNlciIsIklzd0VkaXRBZG1pblVzZXIiLCJJc3dVcGRhdGVBZG1pblVzZXJTdGF0dXMiLCJJc3dBcHByb3ZlT3JSZWplY3RBZG1pblVzZXIiLCJJc3dDcmVhdGVBZG1pblJvbGUiLCJJc3dFZGl0QWRtaW5Sb2xlIiwiSXN3Vmlld0FkbWluUm9sZSIsIklzd1ZpZXdBZG1pblJvbGVzIiwiSXN3Vmlld0FkbWluUGVybWlzc2lvbiIsIklzd1ZpZXdBZG1pblBlcm1pc3Npb25zIiwiSXN3VXBkYXRlQWRtaW5Sb2xlU3RhdHVzIiwiSXN3VXBkYXRlUGVybWlzc2lvblN0YXR1cyIsIklzd1VwZGF0ZUFkbWluUm9sZVBlcm1pc3Npb25zIiwiSXN3Vmlld0FkbWluTW9kdWxlcyIsIklzd1ZpZXdBZG1pbk1vZHVsZSIsIklzd1ZpZXdBZG1pbk1vZHVsZVBlcm1pc3Npb25zIiwiSXN3Vmlld0FkbWluTW9kdWxlUGVybWlzc2lvbiIsIklzd1ZpZXdBZG1pblJvbGVVc2VycyIsIklzd0RlbGV0ZUFkbWluUm9sZSIsIklzd0NyZWF0ZUNvcnBvcmF0ZSIsIklzd0VkaXRDb3Jwb3JhdGUiLCJJc3dWaWV3Q29ycG9yYXRlIiwiSXN3Vmlld0NvcnBvcmF0ZXMiLCJJc3dWaWV3VXNlcnMiLCJJc3dWaWV3VXNlciIsIklzd0VkaXRVc2VyIiwiSXN3VXBkYXRlVXNlclN0YXR1cyIsIklzd0FwcHJvdmVPclJlamVjdFVzZXIiLCJJc3dWaWV3UGVybWlzc2lvbnMiLCJJc3dWaWV3UGVybWlzc2lvbiIsIklzd0NyZWF0ZVJvbGUiLCJJc3dFZGl0Um9sZSIsIklzd1ZpZXdSb2xlIiwiSXN3Vmlld1JvbGVzIiwiSXN3VXBkYXRlUm9sZVBlcm1pc3Npb25zIiwiSXN3Vmlld01vZHVsZXMiLCJJc3dWaWV3TW9kdWxlIiwiSXN3Vmlld01vZHVsZVBlcm1pc3Npb25zIiwiSXN3Vmlld01vZHVsZVBlcm1pc3Npb24iLCJJc3dWaWV3Um9sZVVzZXJzIiwiSXN3RGVsZXRlUm9sZSIsIklzd0RlQWN0aXZhdGVVc2VyIiwiQ3JlYXRlVXNlciIsIlZpZXdVc2VyIiwiRWRpdFVzZXIiLCJWaWV3VXNlcnMiLCJWaWV3VXNlcnMiLCJVcGRhdGVVc2VyU3RhdHVzIiwiQXBwcm92ZU9yUmVqZWN0VXNlciIsIkRlQWN0aXZhdGVVc2VyIiwiQ3JlYXRlUm9sZSIsIkVkaXRSb2xlIiwiRGVsZXRlUm9sZSIsIlZpZXdSb2xlIiwiVmlld1JvbGVzIiwiVmlld1Blcm1pc3Npb25zIiwiVmlld1Blcm1pc3Npb24iLCJVcGRhdGVSb2xlU3RhdHVzIiwiVXBkYXRlUm9sZVBlcm1pc3Npb25zIiwiR2VuZXJhdGVDb2RlIiwiQmxhY2tsaXN0VXNlcnMiLCJCbGFja2xpc3RVc2VyIiwiQ3JlYXRlQ2FtcGFpZ25Db25maWd1cmF0aW9uIiwiRWRpdENhbXBhaWduQ29uZmlndXJhdGlvbiIsIkRlbGV0ZUNhbXBhaWduQ29uZmlndXJhdGlvbiIsIkNyZWF0ZUNhbXBhaWduIiwiRWRpdENhbXBhaWduIiwiVmlld0NhbXBhaWduIiwiVmlld0NhbXBhaWducyIsIlN0YXJ0Q2FtcGFpZ24iLCJQYXVzZUNhbXBhaWduIiwiQ3JlYXRlQ2FtcGFpZ25SZXdhcmQiLCJFZGl0Q2FtcGFpZ25SZXdhcmQiLCJDcmVhdGVSYWZmbGVSZXdhcmQiLCJFZGl0UmFmZmxlUmV3YXJkIiwiRGVsZXRlQ2FtcGFpZ25SZXdhcmQiLCJEZWxldGVSYWZmbGVSZXdhcmQiXSwibW9kdWxlIjpbIklTVyBBZG1pbiBVc2VyIE1hbmFnZW1lbnQgTW9kdWxlIiwiQ29ycG9yYXRlIFVzZXIgTWFuYWdlbWVudCBNb2R1bGUiLCJJU1cgUm9sZS9QZXJtaXNzaW9uIE1hbmFnZW1lbnQgTW9kdWxlIiwiQ29ycG9yYXRlIFJvbGUvUGVybWlzc2lvbiBNYW5hZ2VtZW50IE1vZHVsZSIsIkNvcnBvcmF0ZSBQb2ludCBCYXNlZCBJbnRlcmFjdGlvbiBNb2R1bGUiLCJDb3Jwb3JhdGUgUmV3YXJkIENvZGUvVG9rZW4gSW50ZXJhY3Rpb24gTW9kdWxlIiwiQ29ycG9yYXRlIFRyYW5zYWN0aW9uIEJhc2VkIE1vZHVsZSIsIkNvcnBvcmF0ZSBHaWZ0aW5nIE1vZHVsZSIsIkdpZnRpbmcgTW9kdWxlIiwiVXNlciBNYW5hZ2VtZW50IE1vZHVsZSIsIlJvbGUvUGVybWlzc2lvbiBNYW5hZ2VtZW50IE1vZHVsZSIsIlJld2FyZCBDb2RlL1Rva2VuIEludGVyYWN0aW9uIE1vZHVsZSIsIlBvaW50IEJhc2VkIEludGVyYWN0aW9uIE1vZHVsZSJdLCJ1c2VyX2lkIjoiNiIsIm5hbWUiOiJPbHV3YWtlbWkgQXdvc2lsZSIsImVtYWlsIjoia2VtaWppYmJvbGFAZ21haWwuY29tIiwiY29ycG9yYXRlX2lkIjoiMCIsIm5iZiI6MTYzMTA4OTIzNywiZXhwIjoxNjQxNjg1NjM5LCJpYXQiOjE2MzEwODkyMzd9.fj65Vet8QxQgX10TgHaCwLhRW3iy_lIEvCIVmsBSTDxnfvadz8TiSJQs99eCnP3HGipj7oXcUdAOACnyMEDSZKV7Bmg_75scCb_c_r_LAjvAowAFiKOM7h-FuCaK6BP_ZLYoDL5AKwtkh1OoKlCDgF0h3RKt15ANOCRYwUPiOF7F-6ewe_JomIxeNqQpAR5bgxD3J6k4Ry8qaYJVv1xfJl9WutY7rz5SOvjMLCX3OPN6_RG_JzG4MuNuU6TUJWs-lpxhIbWSUvmiEeIhY_lGnlJ0r7u012ymZfi_cFuM0NrsZBbejXGZ_WNA3-bbKzWGoYYoCRCeiXJu8xZDh2qZTA"));
//
//		codeManagementService.generateCodes(CodeGenerationRequest
//				.builder().algorithm("PREFIX_ALPHANUMERIC").prefixValue("").numberOfCodes(10).codeLength(10).clientName("Guiness").codePerFile(5).build());
//
//		codeManagementService.generateCodes(CodeGenerationRequest
//				.builder().algorithm("PREFIX_NUMERIC").prefixValue("AU").numberOfCodes(50).codeLength(10).clientName("Guiness").codePerFile(5).build());

		//testPasswordFile();
	}

//	private void testPasswordFile(){
//		try {
//			//This is name and path of zip file to be created
//			ZipFile zipFile = new ZipFile("C:/users/chris.imoni/Documents/test.zip");
//			log.info("declared zip file path...");
//
//			//Add files to be archived into zip file
//			ArrayList<File> filesToAdd = new ArrayList<File>();
//			filesToAdd.add(new File("C:/Users/chris.imoni/Documents/uploads/codes/Nestle/20220727085532/hashed_codes/1658908532309_hashed_1.csv"));
//			filesToAdd.add(new File("C:/Users/chris.imoni/Documents/uploads/codes/Nestle/20220727085532/hashed_codes/1658908532309_hashed_2.csv"));
//
//			log.info("finished adding text files to zip file folder...");
//
//			//Initiate Zip Parameters which define various properties
//			ZipParameters parameters = new ZipParameters();
//			parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE); // set compression method to deflate compression
//
//			//DEFLATE_LEVEL_FASTEST     - Lowest compression level but higher speed of compression
//			//DEFLATE_LEVEL_FAST        - Low compression level but higher speed of compression
//			//DEFLATE_LEVEL_NORMAL  - Optimal balance between compression level/speed
//			//DEFLATE_LEVEL_MAXIMUM     - High compression level with a compromise of speed
//			//DEFLATE_LEVEL_ULTRA       - Highest compression level but low speed
//			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
//
//			//Set the encryption flag to true
//			parameters.setEncryptFiles(true);
//
//			//Set the encryption method to AES Zip Encryption
//			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
//
//			//AES_STRENGTH_128 - For both encryption and decryption
//			//AES_STRENGTH_192 - For decryption only
//			//AES_STRENGTH_256 - For both encryption and decryption
//			//Key strength 192 cannot be used for encryption. But if a zip file already has a
//			//file encrypted with key strength of 192, then Zip4j can decrypt this file
//			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
//
//			//Set password
//			parameters.setPassword("howtodoinjava");
//			log.info("finished setting passwords...");
//
//			if(!zipFile.getFile().exists()){
//				log.info("file does not exist==>"+zipFile.getFile().getPath());
//				zipFile.createZipFile(zipFile.getFile(),parameters);
//			}
//			//Now add files to the zip file
//			zipFile.addFiles(filesToAdd, parameters);
//			log.info("finished adding files...");
//		}
//		catch (ZipException e)
//		{
//			e.printStackTrace();
//		}
//	}

//	private void testPasswordFile2(){
//		try {
//
//			ZipParameters parameters = new ZipParameters();
//			parameters.setCompressionLevel(CompressionLevel.NORMAL);
//			parameters.setCompressionMethod(); // set compression method to deflate compression
//
//			//DEFLATE_LEVEL_FASTEST     - Lowest compression level but higher speed of compression
//			//DEFLATE_LEVEL_FAST        - Low compression level but higher speed of compression
//			//DEFLATE_LEVEL_NORMAL  - Optimal balance between compression level/speed
//			//DEFLATE_LEVEL_MAXIMUM     - High compression level with a compromise of speed
//			//DEFLATE_LEVEL_ULTRA       - Highest compression level but low speed
//			parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
//
//			//Set the encryption flag to true
//			parameters.setEncryptFiles(true);
//
//			//Set the encryption method to AES Zip Encryption
//			parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
//
//			//AES_STRENGTH_128 - For both encryption and decryption
//			//AES_STRENGTH_192 - For decryption only
//			//AES_STRENGTH_256 - For both encryption and decryption
//			//Key strength 192 cannot be used for encryption. But if a zip file already has a
//			//file encrypted with key strength of 192, then Zip4j can decrypt this file
//			parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
//
//			//Set password
//			parameters.setPassword("howtodoinjava");
//			log.info("finished setting passwords...");
//
//			if(!zipFile.getFile().exists()){
//				log.info("file does not exist==>"+zipFile.getFile().getPath());
//				zipFile.createZipFile(zipFile.getFile(),parameters);
//			}
//			//Now add files to the zip file
//			zipFile.addFiles(filesToAdd, parameters);
//			log.info("finished adding files...");
//		}
//		catch (ZipException e)
//		{
//			e.printStackTrace();
//		}
//	}
}
