cmake_minimum_required(VERSION 3.22.1)

project("reader")

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99")
set(MU_PDF_VERSION "1.24.1")

add_compile_definitions(DEBUGLOG)


add_subdirectory(libcommon)
add_subdirectory(libhqx-1.2)
add_subdirectory(libmobi-0.11)
add_subdirectory(libdjvu-3.5.28)
add_subdirectory(libunrar)

# 这里需要和克隆下来的带版本号的文件名保持一致
add_subdirectory(libmupdf-${MU_PDF_VERSION})

#include_directories(libmupdf-1.24.1/include)
#include_directories(libmupdf-1.24.1/source/fitz)
#include_directories(libmupdf-1.24.1/source/pdf)
#include_directories(libdjvu-3.5.28/src)
#include_directories(libmobi-0.9/src)
#include_directories(libmobi-0.9/tools)
#include_directories(libhqx-1.2)

# 构建动态库
add_library(${CMAKE_PROJECT_NAME} SHARED
        era.cpp
)

target_compile_definitions(${CMAKE_PROJECT_NAME} PRIVATE HAVE_ANDROID)

# 链接动态库 mu_pdf，静态库 djvu 用了 mu_pdf 内含的 libjepg
target_link_libraries(${CMAKE_PROJECT_NAME} mupdf_java)

# 链接静态库
target_link_libraries(${CMAKE_PROJECT_NAME} -Wl,--whole-archive common -Wl,--no-whole-archive)
target_link_libraries(${CMAKE_PROJECT_NAME} -Wl,--whole-archive mobi -Wl,--no-whole-archive)
target_link_libraries(${CMAKE_PROJECT_NAME} -Wl,--whole-archive hqx -Wl,--no-whole-archive)
target_link_libraries(${CMAKE_PROJECT_NAME} -Wl,--whole-archive djvu -Wl,--no-whole-archive)
target_link_libraries(${CMAKE_PROJECT_NAME} -Wl,--whole-archive unrar -Wl,--no-whole-archive)


#target_compile_options(${CMAKE_PROJECT_NAME} PRIVATE $<$<COMPILE_LANGUAGE:C>: -std=gnu99>)
#target_compile_options(${CMAKE_PROJECT_NAME} PRIVATE $<$<OR:$<COMPILE_LANGUAGE:CXX>,$<COMPILE_LANGUAGE:CPP>>: -std=c++17>)
