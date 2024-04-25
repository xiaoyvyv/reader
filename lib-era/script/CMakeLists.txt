# 设置源文件路径
set(MUPDF_PATH ${CMAKE_CURRENT_SOURCE_DIR})
set(SOURCE_DIR ${MUPDF_PATH}/platform/java)

set(MUPDF_EXTRA_CFLAGS "")
set(MUPDF_EXTRA_LDFLAGS "")
set(MUPDF_EXTRA_LDLIBS "")

set(USE_TESSERACT ON)
set(USE_LEPTONICA ON)

# 设置 NEON 和 AVX 相关的选项
if (${ANDROID_ABI} STREQUAL "arm64-v8a")
    set(HAVE_NEON ON)
elseif (${ANDROID_ABI} STREQUAL "x86_64")
    set(HAVE_AVX ON)
    set(HAVE_AVX2 ON)
    set(HAVE_FMA ON)
    set(HAVE_SSE4_1 ON)
endif ()

message("Options: USE_TESSERACT -> ${USE_TESSERACT}")
message("Options: USE_LEPTONICA -> ${USE_LEPTONICA}")

# 设置 TESSERACT 和 LEPTONICA 相关的选项
if (USE_TESSERACT)

    # 开启 TESSERACT 情况下，强制开启 USE_LEPTONICA
    if (NOT USE_LEPTONICA)
        set(USE_LEPTONICA ON)
    endif ()
endif ()

include(CMakeLibrary.cmake)

# --- Build a local static library for core mupdf ---
file(GLOB MU_PDF_CORE_SRC
        "${MUPDF_PATH}/source/fitz/*.c"
        "${MUPDF_PATH}/source/fitz/*.cpp"
        "${MUPDF_PATH}/source/pdf/*.c"
        "${MUPDF_PATH}/source/xps/*.c"
        "${MUPDF_PATH}/source/svg/*.c"
        "${MUPDF_PATH}/source/cbz/*.c"
        "${MUPDF_PATH}/source/html/*.c"
        "${MUPDF_PATH}/source/helpers/pkcs7/*.c"
        "${MUPDF_PATH}/generated/resources/fonts/urw/*.c"
)

# 添加 mupdf_core 静态库
add_library(mupdf_core STATIC ${MU_PDF_CORE_SRC})

# 设置 mupdf_core 静态库的 include 路径
target_include_directories(mupdf_core PRIVATE ${MUPDF_PATH}/include)
target_include_directories(mupdf_core PRIVATE ${FREETYPE_INCLUDE})
target_include_directories(mupdf_core PRIVATE ${GUMBO_INCLUDE})
target_include_directories(mupdf_core PRIVATE ${HARFBUZZ_INCLUDE})
target_include_directories(mupdf_core PRIVATE ${JBIG2DEC_INCLUDE})
target_include_directories(mupdf_core PRIVATE ${LCMS2_INCLUDE})
target_include_directories(mupdf_core PRIVATE ${LIBJPEG_INCLUDE})
target_include_directories(mupdf_core PRIVATE ${MUJS_INCLUDE})
target_include_directories(mupdf_core PRIVATE ${OPENJPEG_INCLUDE})
target_include_directories(mupdf_core PRIVATE ${TESSERACT_INCLUDE})
target_include_directories(mupdf_core PRIVATE ${LEPTONICA_INCLUDE})
target_include_directories(mupdf_core PRIVATE ${EXTRACT_INCLUDE})

if (USE_TESSERACT)
    target_include_directories(mupdf_core PRIVATE ${TESSERACT_INCLUDE})
endif ()

if (USE_LEPTONICA)
    target_include_directories(mupdf_core PRIVATE ${LEPTONICA_INCLUDE})
endif ()

# mupdf_core defines
target_compile_definitions(mupdf_core PRIVATE _FILE_OFFSET_BITS=32)
target_compile_definitions(mupdf_core PRIVATE TOFU_NOTO)
target_compile_definitions(mupdf_core PRIVATE TOFU_CJK)
target_compile_definitions(mupdf_core PRIVATE TOFU_SIL)
target_compile_definitions(mupdf_core PRIVATE AA_BITS=8)
target_compile_definitions(mupdf_core PRIVATE HAVE_LCMS2MT)
target_compile_definitions(mupdf_core PRIVATE OPJ_STATIC)
target_compile_definitions(mupdf_core PRIVATE OPJ_HAVE_INTTYPES_H)
target_compile_definitions(mupdf_core PRIVATE OPJ_HAVE_STDINT_H)

# mupdf_core options
target_compile_options(mupdf_core PRIVATE -ffunction-sections)
target_compile_options(mupdf_core PRIVATE -fdata-sections)
target_compile_options(mupdf_core PRIVATE ${FREETYPE_BUILD_CFLAGS})
target_compile_options(mupdf_core PRIVATE ${GUMBO_BUILD_CFLAGS})
target_compile_options(mupdf_core PRIVATE ${HARFBUZZ_BUILD_CFLAGS})
target_compile_options(mupdf_core PRIVATE ${JBIG2DEC_BUILD_CFLAGS})
target_compile_options(mupdf_core PRIVATE ${LCMS2_BUILD_CFLAGS})
target_compile_options(mupdf_core PRIVATE ${LIBJPEG_BUILD_CFLAGS})
target_compile_options(mupdf_core PRIVATE ${MUJS_BUILD_CFLAGS})
target_compile_options(mupdf_core PRIVATE ${OPENJPEG_BUILD_CFLAGS})
target_compile_options(mupdf_core PRIVATE ${EXTRACT_BUILD_CFLAGS})
target_compile_options(mupdf_core PRIVATE ${MUPDF_EXTRA_CFLAGS})
target_compile_options(mupdf_core PRIVATE $<$<COMPILE_LANGUAGE:C>: -std=gnu99>)
target_compile_options(mupdf_core PRIVATE $<$<OR:$<COMPILE_LANGUAGE:CXX>,$<COMPILE_LANGUAGE:CPP>>: -std=c++17>)

if (USE_TESSERACT)
    target_compile_definitions(mupdf_core PRIVATE HAVE_TESSERACT)
    target_compile_options(mupdf_core PRIVATE ${TESSERACT_BUILD_CFLAGS})
endif ()

if (USE_LEPTONICA)
    target_compile_definitions(mupdf_core PRIVATE HAVE_LEPTONICA)
    target_compile_options(mupdf_core PRIVATE ${LEPTONICA_BUILD_CFLAGS})
endif ()

# --- Build local static libraries for thirdparty libraries ---

# Freetype
add_library(mupdf_thirdparty_freetype STATIC ${FREETYPE_SRC})
target_include_directories(mupdf_thirdparty_freetype PRIVATE ${FREETYPE_INCLUDE})
target_compile_definitions(mupdf_thirdparty_freetype PRIVATE ${FREETYPE_DEFINES})
target_compile_options(mupdf_thirdparty_freetype PRIVATE ${MUPDF_EXTRA_CFLAGS})

# Gumbo
add_library(mupdf_thirdparty_gumbo STATIC ${GUMBO_SRC})
target_include_directories(mupdf_thirdparty_gumbo PRIVATE ${GUMBO_INCLUDE})
target_compile_options(mupdf_thirdparty_gumbo PRIVATE ${GUMBO_BUILD_CFLAGS} ${MUPDF_EXTRA_CFLAGS})

# JBIG2DEC
add_library(mupdf_thirdparty_jbig2dec STATIC ${JBIG2DEC_SRC})
target_include_directories(mupdf_thirdparty_jbig2dec PRIVATE ${JBIG2DEC_INCLUDE})
target_compile_definitions(mupdf_thirdparty_jbig2dec PRIVATE ${JBIG2DEC_DEFINES})
target_compile_options(mupdf_thirdparty_jbig2dec PRIVATE ${MUPDF_EXTRA_CFLAGS})

# Harfbuzz
add_library(mupdf_thirdparty_harfbuzz STATIC ${HARFBUZZ_SRC})
target_include_directories(mupdf_thirdparty_harfbuzz PRIVATE ${HARFBUZZ_INCLUDE})
target_compile_definitions(mupdf_thirdparty_harfbuzz PRIVATE ${HARFBUZZ_DEFINES})
target_compile_options(mupdf_thirdparty_harfbuzz PRIVATE ${HARFBUZZ_BUILD_CFLAGS} ${MUPDF_EXTRA_CFLAGS})

# Lcms2
add_library(mupdf_thirdparty_lcms2 STATIC ${LCMS2_SRC})
target_include_directories(mupdf_thirdparty_lcms2 PRIVATE ${LCMS2_INCLUDE})
target_compile_definitions(mupdf_thirdparty_lcms2 PRIVATE ${LCMS2_DEFINES})
target_compile_options(mupdf_thirdparty_lcms2 PRIVATE ${MUPDF_EXTRA_CFLAGS})

# Libjpeg
add_library(mupdf_thirdparty_libjpeg STATIC ${LIBJPEG_SRC})
target_include_directories(mupdf_thirdparty_libjpeg PRIVATE ${LIBJPEG_INCLUDE})
target_compile_options(mupdf_thirdparty_libjpeg PRIVATE ${MUPDF_EXTRA_CFLAGS})

# Mujs
add_library(mupdf_thirdparty_mujs STATIC ${MUJS_SRC})
target_include_directories(mupdf_thirdparty_mujs PRIVATE ${MUJS_INCLUDE})
target_compile_options(mupdf_thirdparty_mujs PRIVATE ${MUPDF_EXTRA_CFLAGS})

# Openjpeg
add_library(mupdf_thirdparty_openjpeg STATIC ${OPENJPEG_SRC})
target_include_directories(mupdf_thirdparty_openjpeg PRIVATE ${OPENJPEG_INCLUDE})
target_compile_definitions(mupdf_thirdparty_openjpeg PRIVATE ${OPENJPEG_DEFINES})
target_compile_options(mupdf_thirdparty_openjpeg PRIVATE ${MUPDF_EXTRA_CFLAGS})

# Extract
add_library(mupdf_thirdparty_extract STATIC ${EXTRACT_SRC})
target_include_directories(mupdf_thirdparty_extract PRIVATE ${EXTRACT_INCLUDE})
target_compile_options(mupdf_thirdparty_extract PRIVATE ${MUPDF_EXTRA_CFLAGS})

# Tesseract
if (USE_TESSERACT)
    add_library(mupdf_thirdparty_tesseract STATIC ${TESSERACT_SRC} ${MUPDF_PATH}/source/fitz/tessocr.cpp)
    target_include_directories(mupdf_thirdparty_tesseract PRIVATE ${MUPDF_PATH}/include)
    target_include_directories(mupdf_thirdparty_tesseract PRIVATE ${TESSERACT_INCLUDE})
    target_compile_definitions(mupdf_thirdparty_tesseract PRIVATE ${TESSERACT_DEFINES})
    target_compile_options(mupdf_thirdparty_tesseract PRIVATE ${TESSERACT_BUILD_CFLAGS} -Wno-sign-compare ${MUPDF_EXTRA_CFLAGS})
    target_compile_options(mupdf_thirdparty_tesseract PRIVATE -fexceptions)
endif ()

# Leptonica
if (USE_LEPTONICA)
    add_library(mupdf_thirdparty_leptonica STATIC ${LEPTONICA_SRC} ${MUPDF_PATH}/source/fitz/tessocr.cpp)
    target_include_directories(mupdf_thirdparty_leptonica PRIVATE ${MUPDF_PATH}/include)
    target_include_directories(mupdf_thirdparty_leptonica PRIVATE ${LEPTONICA_INCLUDE})
    target_compile_definitions(mupdf_thirdparty_leptonica PRIVATE ${LEPTONICA_DEFINES} ANDROID_BUILD)
    target_compile_options(mupdf_thirdparty_leptonica PRIVATE ${LEPTONICA_BUILD_CFLAGS} -Wno-sign-compare ${MUPDF_EXTRA_CFLAGS})
endif ()

# --- Build the final JNI shared library ---

add_library(mupdf_java SHARED ${MUPDF_PATH}/platform/java/mupdf_native.c)
target_include_directories(mupdf_java PRIVATE ${MUPDF_PATH}/include)
target_compile_definitions(mupdf_java PRIVATE HAVE_ANDROID)
target_compile_options(mupdf_java PRIVATE ${MUPDF_EXTRA_CFLAGS})

if (ADD_SOURCE_FILES)
    target_sources(mupdf_java PRIVATE ${ADD_SOURCE_FILES})
    target_compile_options(mupdf_java PRIVATE ${ADD_C_FLAGS})
    target_include_directories(mupdf_java PRIVATE ${ADD_C_INCLUDES})
endif ()

target_link_libraries(mupdf_java PRIVATE mupdf_core)
target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_freetype)
target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_gumbo)
target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_harfbuzz)
target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_jbig2dec)
target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_lcms2)
target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_libjpeg)
target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_mujs)
target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_openjpeg)
target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_extract)

if (USE_TESSERACT)
    target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_tesseract)
endif ()

if (USE_LEPTONICA)
    target_link_libraries(mupdf_java PRIVATE mupdf_thirdparty_leptonica)
endif ()

target_link_libraries(mupdf_java PRIVATE ${MUPDF_EXTRA_LDLIBS})
target_link_libraries(mupdf_java PRIVATE jnigraphics)
target_link_libraries(mupdf_java PRIVATE log)
target_link_libraries(mupdf_java PRIVATE z)
target_link_libraries(mupdf_java PRIVATE m)

set_target_properties(mupdf_java PROPERTIES LINK_FLAGS "-Wl,--gc-sections ${MUPDF_EXTRA_LDFLAGS}")
