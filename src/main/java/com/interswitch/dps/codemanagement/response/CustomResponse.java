package com.interswitch.dps.codemanagement.response;


import com.interswitch.dps.codemanagement.utils.DateUtil;
import org.springframework.http.HttpStatus;
import java.util.Date;

/**
 * This is a builder pattern used to build API custom responses.
 *
 * @param <D>
 * @author earnest
 */
public class CustomResponse<D> {

    private final HttpStatus status;
    private final String code;
    private final String message;
    private final String description;
    private final String timestamp;
    private final D data;

    private CustomResponse(CustomResponseBuilder<D> builder) {
        this.status = builder.status;
        this.code = builder.code;
        this.message = builder.message;
        this.description = builder.description;
        this.timestamp = builder.timestamp;
        this.data = builder.data;

    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getDescription() {
        return this.description;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public D getData() {
        return data;
    }


    public static final class CustomResponseBuilder<D> {
        private HttpStatus status;
        private String code;
        private String message;
        private String description;
        private String timestamp;
        private D data;

        public CustomResponseBuilder<D> withStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public CustomResponseBuilder<D> withCode(String code) {
            this.code = code;
            return this;
        }

        public CustomResponseBuilder<D> withMessage(String message) {
            this.message = message;
            return this;
        }

        public CustomResponseBuilder<D> withDetail(String detail) {
            this.description = detail;
            return this;
        }

        public CustomResponseBuilder<D> withTimestamp(Date timestamp) {
            this.timestamp = DateUtil.convertToString(timestamp);
            return this;
        }

        public CustomResponseBuilder<D> withData(D data) {
            this.data = data;
            return this;
        }

        public CustomResponse<D> build() {
            return new CustomResponse<D>(this);
        }
    }
}
