package com.pinterest.l10nmessages;

public enum OnFormatErrors implements OnFormatError {
  RETHROW {
    @Override
    public String apply(Throwable throwable, String baseName, String key, String message)
        throws RuntimeException {
      throw new OnFormatErrorException(throwable);
    }
  },
  MESSAGE_FALLBACK {
    @Override
    public String apply(Throwable throwable, String baseName, String key, String message)
        throws RuntimeException {
      return message == null ? key : message;
    }
  }
}
