add_library(unrar STATIC
        src/dmc_unrar.c
        jni-rar.cpp
)

target_include_directories(unrar PRIVATE
        include
        src
        ../libcommon
)

target_link_libraries(unrar PRIVATE common)