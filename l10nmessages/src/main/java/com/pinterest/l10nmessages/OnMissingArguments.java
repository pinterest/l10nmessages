package com.pinterest.l10nmessages;

import java.util.Optional;

public enum OnMissingArguments implements OnMissingArgument {
  NOOP {
    @Override
    public Optional<Object> apply(String baseName, String key, String argumentName) {
      return Optional.empty();
    }
  },
  THROW {
    @Override
    public Optional<Object> apply(String baseName, String key, String argumentName) {
      throw new OnMissingArgumentException(
          String.format(
              "Argument: `%3$s` missing for baseName: `%1$s` and key: `%2$s`",
              baseName, key, argumentName));
    }
  };
}
