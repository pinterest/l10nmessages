package com.pinterest.l10nmessages.tests;

import static com.pinterest.l10nmessages.tests.perf.Strings.favorite_numbers;
import static com.pinterest.l10nmessages.tests.perf.Strings.five_dates;
import static com.pinterest.l10nmessages.tests.perf.Strings.no_format;
import static com.pinterest.l10nmessages.tests.perf.Strings.one_plural_string;
import static com.pinterest.l10nmessages.tests.perf.Strings.single_date;

import com.pinterest.l10nmessages.EnumType;
import com.pinterest.l10nmessages.FormatContext;
import com.pinterest.l10nmessages.L10nMessages;
import com.pinterest.l10nmessages.L10nProperties;
import com.pinterest.l10nmessages.MessageFormatAdapterProviders;
import com.pinterest.l10nmessages.MessageFormatLoadingCacheProviders;
import com.pinterest.l10nmessages.tests.perf.Strings;
import com.pinterest.l10nmessages.tests.perf.Strings.FC_favorite_numbers;
import com.pinterest.l10nmessages.tests.perf.Strings.FC_five_dates;
import com.pinterest.l10nmessages.tests.perf.Strings.FC_one_plural_string;
import com.pinterest.l10nmessages.tests.perf.Strings.FC_one_username;
import com.pinterest.l10nmessages.tests.perf.Strings.FC_single_date;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

@L10nProperties(
    baseName = "com.pinterest.l10nmessages.tests.perf.Strings",
    enumType = EnumType.WITH_ARGUMENT_BUILDERS,
    messageFormatAdapterProviders = MessageFormatAdapterProviders.ICU)
public class BenchmarkRunner {

  @State(Scope.Benchmark)
  public static class BenchmarkState {

    L10nMessages<Strings> withoutCache = L10nMessages.builder(Strings.class).build();
    L10nMessages<Strings> withCache =
        L10nMessages.builder(Strings.class)
            .messageFormatLoadingCacheProvider(
                MessageFormatLoadingCacheProviders.CONCURRENT_HASH_MAP)
            .build();

    L10nMessages getL10nMessages() {
      switch (l10nmessages) {
        case "withCache":
          return withCache;
        case "withoutCache":
          return withoutCache;
        default:
          throw new IllegalStateException("wrong l10nmessages: " + l10nmessages);
      }
    }

    FormatContext<Strings> fc_no_format =
        new FormatContext<Strings>() {

          LinkedHashMap map = new LinkedHashMap<String, Object>();

          @Override
          public Strings getKey() {
            return no_format;
          }

          @Override
          public Map<String, Object> getArguments() {
            return map;
          }
        };

    FC_one_username fc_one_username = Strings.one_username().username("Mary");
    FC_single_date fc_single_date = single_date().date(new Date());
    FC_five_dates fc_five_dates =
        five_dates()._0(new Date())._1(new Date())._2(new Date())._3(new Date())._4(new Date());
    FC_one_plural_string fc_one_plural_string = one_plural_string().catCount(10);

    FC_favorite_numbers fc_favorite_numbers =
        favorite_numbers().userGender("female").numbers("1").numbersCount(1);

    @Param({
      "fc_no_format",
      "fc_one_username",
      "fc_single_date",
      "fc_five_dates",
      "fc_one_plural_string",
      "fc_favorite_numbers"
    })
    String context;

    @Param({
      "withCache",
      "withoutCache",
    })
    String l10nmessages;

    FormatContext<Strings> getFormatContext() {
      switch (context) {
        case "fc_no_format":
          return fc_no_format;
        case "fc_one_username":
          return fc_one_username;
        case "fc_single_date":
          return fc_single_date;
        case "fc_five_dates":
          return fc_five_dates;
        case "fc_one_plural_string":
          return fc_one_plural_string;
        case "fc_favorite_numbers":
          return fc_favorite_numbers;
      }
      throw new IllegalStateException("wrong context: " + context);
    }
  }

  @Benchmark
  @BenchmarkMode({Mode.AverageTime})
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public String format(BenchmarkState state) {
    return state.getL10nMessages().format(state.getFormatContext());
  }

  public static void main(String... args) throws Exception {

    Options opt =
        new OptionsBuilder()
            .include(BenchmarkRunner.class.getSimpleName())
            //        .addProfiler(GCProfiler.class)
            //            .addProfiler(JavaFlightRecorderProfiler.class)
            .mode(Mode.SingleShotTime)
            .warmupIterations(2)
            .warmupTime(TimeValue.seconds(5))
            .measurementIterations(3)
            .measurementTime(TimeValue.seconds(10))
            .forks(1)
            .build();

    new Runner(opt).run();
  }
}
