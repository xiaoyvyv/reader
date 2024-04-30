add_library(common STATIC logger.cpp)

target_link_libraries(common android log)