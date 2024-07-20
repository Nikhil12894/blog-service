package com.nk.blog.exception;

public class DuplicateTransactionException extends RuntimeException{
    public DuplicateTransactionException(String message){super(message);}
}
