filegroup(
    name = "l10nresources",
    srcs = glob(["src/main/resources/**/*.properties"])
)

java_plugin(
    name = "l10nmessages_proc",
    processor_class = "com.pinterest.l10nmessages.L10nPropertiesProcessor",
    deps = [
        "@maven//:com_pinterest_l10nmessages_l10nmessages_proc",
        "@maven//:com_pinterest_l10nmessages_l10nmessages"
    ],
    resources = ["l10nresources"]
)

java_binary(
    name = "l10nbazel",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = ["l10nresources"],
    plugins = [ "l10nmessages_proc"],
    deps = [
           "@maven//:com_pinterest_l10nmessages_l10nmessages",
    ],
    main_class = "com.pinterest.l10nmessages.example.Application",
)