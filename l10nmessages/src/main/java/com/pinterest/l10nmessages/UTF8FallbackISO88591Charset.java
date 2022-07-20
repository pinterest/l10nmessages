package com.pinterest.l10nmessages;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

/**
 * Charset with a decoder that attempts to decode as UTF-8 and then falls back to ISO-88991 when it
 * can't. This is meant to be used to read older Java properties files and to backport Java 9
 * properties files loading in {@link java.util.PropertyResourceBundle}.
 */
public class UTF8FallbackISO88591Charset extends Charset {

  public UTF8FallbackISO88591Charset() {
    super(UTF8FallbackISO88591Charset.class.getCanonicalName(), null);
  }

  @Override
  public boolean contains(Charset cs) {
    return false;
  }

  @Override
  public CharsetDecoder newDecoder() {
    return new UTF8FallbackISO88591Decoder(this);
  }

  @Override
  public CharsetEncoder newEncoder() {
    return StandardCharsets.UTF_8.newEncoder();
  }

  private final class UTF8FallbackISO88591Decoder extends CharsetDecoder {

    private final CharsetDecoder utf8Decoder =
        StandardCharsets.UTF_8
            .newDecoder()
            .onMalformedInput(CodingErrorAction.REPORT)
            .onUnmappableCharacter(CodingErrorAction.REPORT);

    private CharsetDecoder iso88591Decoder = null;

    protected UTF8FallbackISO88591Decoder(Charset cs) {
      super(cs, 1.0f, 1.0f);
    }

    protected CoderResult decodeLoop(ByteBuffer in, CharBuffer out) {
      CoderResult cr;
      if (iso88591Decoder != null) {
        cr = iso88591Decoder.decode(in, out, false);
      } else {
        in.mark();
        out.mark();
        cr = utf8Decoder.decode(in, out, false);
        if (cr.isMalformed() || cr.isUnmappable()) {
          in.reset();
          out.reset();
          iso88591Decoder = StandardCharsets.ISO_8859_1.newDecoder();
          cr = iso88591Decoder.decode(in, out, false);
        }
      }
      return cr;
    }
  }
}
