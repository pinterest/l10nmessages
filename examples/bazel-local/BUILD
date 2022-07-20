filegroup(
    name = "l10nresources",
    srcs = glob(["src/main/resources/**/*.properties"])
)

java_plugin(
    name = "l10nmessages_proc",
    processor_class = "com.pinterest.l10nmessages.L10nPropertiesProcessor",
    # because this example uses local jars we need to explicitly specify "//l10nmessages_local:l10nmessages"
    deps = ["@l10nmessages_local//:l10nmessages-proc", "@l10nmessages_local//:l10nmessages"],
    resources = ["l10nresources"]
)

java_binary(
    name = "l10nbazel",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = ["l10nresources"],
    plugins = [ "l10nmessages_proc"],
    deps = [
           "@l10nmessages_local//:l10nmessages",
    ],
    main_class = "com.pinterest.l10nmessages.example.Application",
)