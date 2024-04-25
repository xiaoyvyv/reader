cmake_minimum_required(VERSION 3.22.1)

project("era")

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -std=c99")

add_subdirectory(liblogger)
add_subdirectory(libmobi-0.11)
add_subdirectory(libhqx-1.2)

# MuPDF 版本号通配兼容
file(GLOB MUPDF_DIRS "${CMAKE_CURRENT_SOURCE_DIR}/libmupdf-*")
foreach (MUPDF_DIR ${MUPDF_DIRS})
    add_subdirectory(${MUPDF_DIR})
endforeach ()

add_library(${CMAKE_PROJECT_NAME} SHARED
        era.cpp)

target_link_libraries(${CMAKE_PROJECT_NAME}
        logger
        mobi
        hqx)