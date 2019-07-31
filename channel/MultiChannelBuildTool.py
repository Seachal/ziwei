#!/usr/bin/python
# coding=utf-8
import zipfile
import shutil
import os
import xlrd
import sys

reload(sys)   
sys.setdefaultencoding('utf8') 

#当前目录
cur_dir = sys.path[0]

#输出目录
out_dir = 'laka-apks'

tmp_dir = os.path.abspath(os.path.join(cur_dir,os.path.pardir))

out_dir = os.path.abspath(os.path.join(tmp_dir,os.path.pardir)) + '/' + out_dir

# 目录不存在则创建
if not os.path.exists(out_dir):
    os.mkdir(out_dir)

# 空文件 便于写入此空文件到apk包中作为channel文件
src_empty_file = cur_dir + '/info/czt.txt'
# 创建一个空文件（不存在则创建）
f = open(src_empty_file, 'w') 
f.close()

print('cur_dir : ' + cur_dir)

# 获取当前目录中所有的apk源包
src_apks = []
# python3 : os.listdir()即可，这里使用兼容Python2的os.listdir('.')
for file in os.listdir(cur_dir):
    fulldirfile = os.path.join(cur_dir, file)
    print('file : ' + file)
    if os.path.isfile(fulldirfile):
        extension = os.path.splitext(file)[1][1:]
        print('extension : ' + extension)
        if extension in 'apk':
            src_apks.append(fulldirfile)

# 获取渠道列表
# channel_file = 'info/channel.txt'
# f = open(channel_file)
# lines = f.readlines()
# f.close()

#读渠道excel
channel_file = cur_dir + '/info/channels.xls'
data = xlrd.open_workbook(channel_file)
table = data.sheets()[0]
nrows = table.nrows


for src_apk in src_apks:
    # file name (with extension)
    src_apk_file_name = os.path.basename(src_apk)
    # 分割文件名与后缀
    temp_list = os.path.splitext(src_apk_file_name)
    # name without extension
    src_apk_name = temp_list[0]
    # 后缀名，包含.   例如: ".apk "
    src_apk_extension = temp_list[1]
    
    # 创建生成目录,与文件名相关
    output_dir = out_dir + '/' + src_apk_name + '/'

    print('output_dir : ' + output_dir)
    
    # 目录不存在则创建
    if not os.path.exists(output_dir):
        os.mkdir(output_dir)
        
    # 遍历渠道号并创建对应渠道号的apk文件
    # for line in lines:
        # 获取当前渠道号，因为从渠道文件中获得带有\n,所有strip一下
        # target_channel = line.strip()
        # 拼接对应渠道号的apk
        # target_apk = output_dir + src_apk_name + "-" + target_channel + src_apk_extension  
        # 拷贝建立新apk
        # shutil.copy(src_apk,  target_apk)
        # zip获取新建立的apk文件
        # zipped = zipfile.ZipFile(target_apk, 'a', zipfile.ZIP_DEFLATED)
        # 初始化渠道信息
        # empty_channel_file = "META-INF/cztchannel_{channel}".format(channel = target_channel)
        # 写入渠道信息
        # zipped.write(src_empty_file, empty_channel_file)
        # 关闭zip流
        # zipped.close()
        
    # 遍历渠道号并创建对应渠道号的apk文件
    for i in range(1, nrows):
        # 获取当前渠道id
        target_channel_id = str(table.cell(i,0).value)

        # 获取当前渠道名
        target_channel_name = str(table.cell(i,1).value)
        # 拼接对应渠道号的apk
        target_apk = output_dir + src_apk_name + "-" + target_channel_id + "-" + target_channel_name + src_apk_extension
        # 拷贝建立新apk
        shutil.copy(src_apk,  target_apk)
        # zip获取新建立的apk文件
        zipped = zipfile.ZipFile(target_apk, 'a', zipfile.ZIP_DEFLATED)
        # 初始化渠道信息
        empty_channel_file = "META-INF/cztchannel_{channel}".format(channel = target_channel_id)
        # 写入渠道信息
        zipped.write(src_empty_file, empty_channel_file)
        # 关闭zip流
        zipped.close()

        
