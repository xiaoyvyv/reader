# 定义源文件
set(SOURCE_FILES_MOBI
        jni-mobi.c
        src/buffer.c
        src/compression.c
        src/debug.c
        src/encryption.c
        src/index.c
        src/memory.c
        src/meta.c
        src/miniz.c
        src/opf.c
        src/parse_rawml.c
        src/read.c
        src/sha1.c
        src/structure.c
        src/util.c
        src/write.c
        src/xmlwriter.c
        src/randombytes.c
        tools/common.c
        tools/mobitool.c
)

add_library(mobi STATIC ${SOURCE_FILES_MOBI})

target_compile_definitions(mobi PRIVATE USE_MINIZ USE_ENCRYPTION USE_XMLWRITER)

# 设置包含目录
target_include_directories(mobi PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/src
        ${CMAKE_CURRENT_SOURCE_DIR}/tools
        ${CMAKE_CURRENT_SOURCE_DIR}/../libcommon
)
