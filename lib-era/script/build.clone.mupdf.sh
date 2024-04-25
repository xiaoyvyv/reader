#!/bin/zsh

# 代理，可选（可以加快克隆速度）
export https_proxy=http://127.0.0.1:7890 http_proxy=http://127.0.0.1:7890 all_proxy=socks5://127.0.0.1:7890

# 这里 TAG 不一样，克隆下来的源码根目录猴后缀也不一样。
# 在 CmakeLists.txt 配置 MuPDF 编译时，需要注意改下文件夹名称的版本号
muPdfTag=1.24.1
cloneIntoDir=../src/main/cpp/libmupdf-$muPdfTag

# 克隆 MuPDF 源码
git clone --recurse-submodules --depth 1 https://github.com/ArtifexSoftware/mupdf.git --branch $muPdfTag $cloneIntoDir  || echo "检测到 $cloneIntoDir 目录已经有文件存在，跳过克隆数据，如果数据不完整请清空该文件夹后重新运行该脚本！"

# 复制编译 CMakeLists.txt 到 MuPDF 源码目录
cp ./CMakeLibrary.cmake ./CMakeLists.txt $cloneIntoDir

# 删除 .git
chmod -R 777 $cloneIntoDir
find $cloneIntoDir -type d -name ".git" -exec rm -rf {} \;
find $cloneIntoDir -type f -name ".git" -exec rm -rf {} \;
find $cloneIntoDir -type f -name ".gitmodules" -exec rm -rf {} \;
