package com.pickyeaters.logic.bean.reply;

import com.pickyeaters.logic.exception.ResultErrorException;

public class Result<T> {

    static private final int STATUS_GENERIC_OK = 0;
    static private final int STATUS_GENERIC_ERROR = 1;

    private final int status;
    private final T value;
    private final String errorMessage;

    private Result(T value, String errorMessage, int status) {
        this.value = value;
        this.errorMessage = errorMessage;
        this.status = status;
    }

    public static <T> Result<T> ok(T value) {
        return new Result<>(value, "", STATUS_GENERIC_OK);
    }

    public static <T> Result<T> error(String errorMessage, int status) {
        return new Result<>(null, errorMessage, status);
    }

    public static <T> Result<T> error(String errorMessage) {
        return error(errorMessage, STATUS_GENERIC_ERROR);
    }

    public T getValue() throws ResultErrorException {
        if(isError()) {
            throw new ResultErrorException(errorMessage);
        }
        return value;
    }

    public boolean isOK() {
        return status == STATUS_GENERIC_OK;
    }

    public boolean isError() {
        return !isOK();
    }

    public String showError() {
        return this.errorMessage;
    }
}
