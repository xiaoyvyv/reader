## mobitool
    usage: /Users/baf/src/libmobi/tools/.libs/mobitool [-cdehimrstuvx7] [-o dir] [-p pid] [-P serial] filename
        不带参数打印帮助文档元数据并退出
        -c        显示封面
        -d        显示原始文本记录
        -e        创建 EPUB 文件（使用 -s 将会显示 EPUB 源文件）
        -h        显示此用法摘要并退出
        -i        打印详细元数据
        -m        打印记录元数据
        -o dir    将输出保存到 dir 文件夹
        -p pid    设置解密的 PID
        -P serial 设置设备序列号以进行解密
        -r        显示原始记录
        -s        显示重建的源文件
        -t        将混合文件拆分为两部分
        -u        显示资源使用情况
        -v        显示版本并退出
        -x        提取转换源和日志（如果存在）
        -7        解析混合文件的 KF7 部分（默认情况下解析 KF8 部分）

## mobimeta
    usage: mobimeta [-a | -s meta=value[,meta=value,...]] [-d meta[,meta,...]] [-p pid] [-P serial] [-hv] filein [fileout]
        不带参数打印帮助文档元数据并退出
        -a ?           列出有效的元数据键名
        -a meta=value  添加元数据
        -d meta        删除元数据
        -s meta=value  设置元数据
        -p pid         设置解密的 PID
        -P serial      设置设备序列号以进行解密
        -h             显示此用法摘要并退出
        -v             显示版本并退出

## mobidrm
    usage: mobidrm [-d | -e] [-hv] [-p pid] [-f date] [-t date] [-s serial] [-o dir] filename
        不带参数打印文档元数据并退出
    
        解密选项：
        -d        解密（必填）
        -p pid    设置解密 PID（可以指定多次）
        -s serial 设置设备序列号（可以指定多次）
    
        加密选项：
        -e        加密（必填）
        -s serial 设置设备序列号（可以指定多次）
        -f date   设置加密有效期起始日期（yyyy-mm-dd）（含）
        -t date   设置加密有效期截止日期（yyyy-mm-dd）（含）
    
        通用选项：
        -o dir    将输出保存到 dir 文件夹
        -h        显示此用法摘要并退出
        -v        显示版本并退出
    