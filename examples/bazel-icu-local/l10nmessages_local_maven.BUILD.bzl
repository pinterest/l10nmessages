java_import(
    name = "l10nmessages",
    jars = [
       "l10nmessages/target/l10nmessages-1.0.3-SNAPSHOT.jar",
    ],
    visibility = ["//visibility:public"],
)

java_import(
    name = "l10nmessages-proc",
    jars = [
       "l10nmessages-proc/target/l10nmessages-proc-1.0.3-SNAPSHOT.jar",
    ],
    visibility = ["//visibility:public"],
)
