# 设置C源文件列表
set(SOURCE_FILES
        init.c
        hq2x.c
        hq3x.c
        hq4x.c
        jni-hqx.c
)

# 添加静态库
add_library(hqx STATIC ${SOURCE_FILES})