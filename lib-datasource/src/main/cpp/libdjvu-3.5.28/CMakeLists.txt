set(DJVU_SOURCE_FILES
        DjvuDroidBridge.cpp
        src/Arrays.cpp
        src/BSByteStream.cpp
        src/BSEncodeByteStream.cpp
        src/ByteStream.cpp
        src/DataPool.cpp
        src/DjVmDir.cpp
        src/DjVmDir0.cpp
        src/DjVmDoc.cpp
        src/DjVmNav.cpp
        src/DjVuAnno.cpp
        src/DjVuDocument.cpp
        src/DjVuDumpHelper.cpp
        src/DjVuErrorList.cpp
        src/DjVuFile.cpp
        src/DjVuFileCache.cpp
        src/DjVuGlobal.cpp
        src/DjVuGlobalMemory.cpp
        src/DjVuImage.cpp
        src/DjVuInfo.cpp
        src/DjVuMessage.cpp
        src/DjVuMessageLite.cpp
        src/DjVuNavDir.cpp
        src/DjVuPalette.cpp
        src/DjVuPort.cpp
        src/DjVuText.cpp
        src/DjVuToPS.cpp
        src/GBitmap.cpp
        src/GContainer.cpp
        src/GException.cpp
        src/GIFFManager.cpp
        src/GMapAreas.cpp
        src/GOS.cpp
        src/GPixmap.cpp
        src/GRect.cpp
        src/GScaler.cpp
        src/GSmartPointer.cpp
        src/GString.cpp
        src/GThreads.cpp
        src/GURL.cpp
        src/GUnicode.cpp
        src/IFFByteStream.cpp
        src/IW44Image.cpp
        src/IW44EncodeCodec.cpp
        src/JB2Image.cpp
        src/JPEGDecoder.cpp
        src/MMRDecoder.cpp
        src/MMX.cpp
        src/UnicodeByteStream.cpp
        src/XMLParser.cpp
        src/XMLTags.cpp
        src/ZPCodec.cpp
        src/atomic.cpp
        src/debug.cpp
        src/ddjvuapi.cpp
        src/miniexp.cpp
)

add_library(djvu STATIC ${DJVU_SOURCE_FILES})

target_include_directories(djvu PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}
        ${CMAKE_CURRENT_SOURCE_DIR}/src
        ${CMAKE_CURRENT_SOURCE_DIR}/../libcommon
        ${CMAKE_CURRENT_SOURCE_DIR}/../libmupdf-${MU_PDF_VERSION}/thirdparty/libjpeg
        ${CMAKE_CURRENT_SOURCE_DIR}/../libmupdf-${MU_PDF_VERSION}/scripts/libjpeg
)

target_compile_options(djvu PRIVATE -fexceptions)

target_compile_definitions(djvu PRIVATE HAVE_CONFIG_H)

target_link_libraries(djvu jnigraphics)