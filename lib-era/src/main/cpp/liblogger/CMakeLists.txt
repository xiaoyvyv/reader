add_library(logger SHARED logger.cpp)

target_link_libraries(logger
        android
        log)