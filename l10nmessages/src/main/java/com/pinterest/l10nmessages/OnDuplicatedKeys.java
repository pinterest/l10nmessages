package com.pinterest.l10nmessages;

/** Defines the behavior when the annotation processor find duplicated keys. */
public enum OnDuplicatedKeys implements OnDuplicatedKey {
  /** Accept duplicates regardless of their value */
  ACCEPT {
    @Override
    public void apply(String resourceName, String key, String value, String previousValue) {}
  },
  /** Accept only if values are the same */
  ACCEPT_IF_SAME {
    @Override
    public void apply(String resourceName, String key, String value, String previousValue) {
      if (!value.equals(previousValue)) {
        throw new OnDuplicatedKeyException(
            String.format(
                "Duplicated key '%1$s'. Fix '%2$s'. Alternatively, use OnDuplicatedKeys#ACCEPT",
                key, resourceName));
      }
    }
  },
  /** Always reject regardless of the value */
  REJECT {
    @Override
    public void apply(String resourceName, String key, String value, String previousValue) {
      throw new OnDuplicatedKeyException(
          String.format(
              "Duplicated key '%1$s'. Fix '%2$s'. Alternatively, use OnDuplicatedKeys#ACCEPT or OnDuplicatedKeys#ACCEPT_IF_SAME",
              key, resourceName));
    }
  }
}
