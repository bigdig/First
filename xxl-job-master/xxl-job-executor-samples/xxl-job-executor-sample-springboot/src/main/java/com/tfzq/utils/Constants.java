package com.tfzq.utils;

public class Constants {
	//客户级别
	public static String CUST_LEVEL_1 = "904580650";//第一梯队
	public static String CUST_LEVEL_2 = "904580651";//第二梯队
	public static String CUST_LEVEL_3 = "904580652";//第三梯队
	public static String CUST_LEVEL_OTHER = "908711355";//其他
	public static String CUST_LEVEL_4 = "1411707804";//第四梯队
	
	
	public static String LABEL_LISTCOMPANY_CAT_ID = "000000001";//标签上市公司
	public static String LABEL_LISTCOMPANY_CAT_NAME = "上市公司";//标签上市公司名称
	public static String LABEL_OTHER_CAT_ID = "000000001";//标签其他分类
	public static String LABEL_OTHER_CAT_NAME = "其他类别";//标签其他分类名称

	public static String DATA_SRC_HANDLED = "[导入已处理]";//导入已处理
	
	//机构表：客户类型
	public static String CUST_CAT_INSURANCE = "297e62b35c67d4f1015c7217dc260e7f";//保险资管1
	public static String CUST_CAT_PUBLIC = "7f56d7880bd64d61ac0e53aa55f38878";//公募基金2
	public static String CUST_CAT_MANAGEMENT = "297e62b35c67d4f1015c7217dc260e7e";//券商资管3
	public static String CUST_CAT_SELF = "297e62b35c67d4f1015c7217dc260e7d";//券商自营4
	public static String CUST_CAT_PRIVATE = "fc34100439464d4db7ec563917f04351";//私募基金5
	public static String CUST_CAT_TRUST_COMPANY = "bbd0e779cc5d455490bc5a7ed5dce573";//信托公司6
	public static String CUST_CAT_OTHER = "33b248b4dfa940f6a94b9a8b4e93167d";//其他7
	public static String CUST_CAT_14 = "cc0fe1aa2f884bea918cd9f8671f95d0";//专业媒体--公募基金子公司14
	public static String CUST_CAT_10 = "7ea1099e1a544f8681a12110e4430556";//非上市公司--财务公司10
	public static String CUST_CAT_8 = "a050489839114707b395551f28c3dd45";//上市公司--海外投资机构8
	public static String CUST_CAT_11 = "9af5c7daa46d491e838069c68da32b9a";//投资公司--PE/VC11
	public static String CUST_CAT_13 = "2c923f88134a4870ab77c5e1159be482";//财富管理公司--大型集团资产管理公司13
	public static String CUST_CAT_9 = "2a25ec8f92a54249b31366ca308730ed";//银行9
	public static String CUST_CAT_15 = "2d0b6fd42ca649ce8d6fea4d6edfb746";//证券公司15
	public static String CUST_CAT_12 = "cc8e64b867104ae08df7a7e2b2f57fc8";//QFII12
	public static String CUST_CAT_16 = "a7c8fe5e33a747dbb03c5dbab2726816";//保险公司16

	//机构表：客户签约状态
	public static String CUST_STATUS_UNSIGNED = "4028abd7565365fa0156675777d40ad3";//拟签约1
	public static String CUST_STATUS_POTENTIAL = "4028abd7565365fa0156675777d40ad4";//潜在客户2
	public static String CUST_STATUS_SIGNED = "4028abd7565365fa0156675777d40ad2";//已签约3
	public static String CUST_STATUS_OTHER = "4028abd7565365fa0156675777d40ad5";//其他4
	
	//机构表：客户等级
	public static String KM_CUST_LEVEL_1 = "8a2894b34d6fd1e7014d7525322804ce";//第一梯队1（核心）
	public static String KM_CUST_LEVEL_2 = "4028abd7565365fa0156674e2fcf0aa8";//第二梯队2（重点）
	public static String KM_CUST_LEVEL_3 = "8a2894b34d6fd1e7014d7525322804d0";//第三梯队3（基础）
	public static String KM_CUST_LEVEL_4 = "8a2894b34d6fd1e7014d7525322804cf";//第四梯队4（一般）
	public static String KM_CUST_LEVEL_10 = "4028abd7565365fa0156674e2fcf0aa7";//其他10（其他）
	
	//机构表：地域
	public static String AREA_CODE_1 = "3c22403b5cc11a70e0530100007f2137";//华东
	public static String AREA_CODE_2 = "3c22403b5cc21a70e0530200007f2137";//华南
	public static String AREA_CODE_3 = "3c22403b5cc31a70e0530300007f2137";//华中
	public static String AREA_CODE_4 = "3c22403b5cc51a70e0530400007f2137";//华北
	public static String AREA_CODE_5 = "3c22403b5cc51a70e0530500007f2137";//西北
	public static String AREA_CODE_6 = "3c22403b5cc51a70e0530600007f2137";//西南
	public static String AREA_CODE_7 = "3c22403b5cc51a70e0530700007f2137";//东北
	public static String AREA_CODE_8 = "3c22403b5cc51a70e0530800007f2137";//台港澳
	
	//客户表：研究行业
	public static String INDUSTRY_1 = "f846e1403c084d188cc7d41fee0b7258";//传媒互联网
	public static String INDUSTRY_2 = "35e21493d65048668f5c59622623a900";//电新
	public static String INDUSTRY_3 = "676d1654fc864b388f34c6db2ee69aae";//电子
	public static String INDUSTRY_4 = "5f8ade88b246415db1fa14c222a74966";//房地产
	public static String INDUSTRY_5 = "464a006e97a943309eda992ec21ec2e5";//纺织服装
	public static String INDUSTRY_6 = "4028abd7576531cf01576625d73700c6";//非银
	public static String INDUSTRY_7 = "297e62b35a8eef75015aac097bf6267e";//钢铁
	public static String INDUSTRY_8 = "74ec1b4672874b169a79fc60034b6ec3";//固定收益
	public static String INDUSTRY_9 = "4028abd7576531cf0157663231c10114";//海外研究
	public static String INDUSTRY_10 = "297e62b3581fd21201581fe9b20100d2";//宏观
	public static String INDUSTRY_11 = "4028abd7576531cf015766379e28014d";//环保公用
	public static String INDUSTRY_12 = "7eb57ef564ee457d98264ab997b07100";//机械军工
	public static String INDUSTRY_13 = "297e62b3597d74b901598124988d1143";//基础化工
	public static String INDUSTRY_14 = "297e62b359a7086b0159ab80bd010f7b";//计算机
	public static String INDUSTRY_15 = "3f29c0bd965345d4ad98be6331fed19b";//家电
	public static String INDUSTRY_16 = "297e62b35a8eef75015aac2440f126eb";//建筑材料
	public static String INDUSTRY_17 = "297e62b35a8eef75015acb46259c3585";//建筑工程
	public static String INDUSTRY_18 = "9047aa150ca843f091ab58d9e5e97c76";//交通运输
	public static String INDUSTRY_19 = "4bbd7436f0e6423bbb9a28ee44ab4400";//金融工程
	public static String INDUSTRY_20 = "4028abd7576531cf015766245c6d00ba";//煤炭
	public static String INDUSTRY_21 = "297e62b3597d74b9015982e615f021b4";//农林牧渔
	public static String INDUSTRY_22 = "4a76778d21b64ab982d335db63e61030";//汽车
	public static String INDUSTRY_23 = "297e62b3581fd2120158200d762f013b";//轻工
	public static String INDUSTRY_24 = "e568093f2b8343518014f34455b4c028";//商社
	public static String INDUSTRY_25 = "297e62b35d1c5a09015d2a1106e72bd9";//石油化工
	public static String INDUSTRY_26 = "c389cb6d770c46e4b388e3ced262626a";//食品饮料
	public static String INDUSTRY_27 = "297e62b3598cdfb90159970856a81ec6";//通信
	public static String INDUSTRY_28 = "d0b0959032954242b43a90544745a5b9";//投资策略
	public static String INDUSTRY_29 = "a8c52e0914e64c22ae9422227b0b98f9";//医药
	public static String INDUSTRY_30 = "4028abd7576531cf01576634af49012e";//银行
	public static String INDUSTRY_31 = "a0f9604191a4447db92940da7b829fbc";//有色金属
	public static String INDUSTRY_32 = "297e62b35b7bb0c5015b84b06a862e95";//中小市值
	public static String INDUSTRY_33 = "ab2edd13ae53452cb82d8fa11766f9a6";//所有行业【对应：投资经理（外）】

	//报告发送组
	public static String SENDGROUP_1 = "1aa4991859e7442d84eae017818839a2";//晨会纪要专用发送组
	public static String SENDGROUP_2 = "5ef61ac3597e4eb293e63c134b43e695";//天风默认宏观（只发送宏观报告）
	public static String SENDGROUP_3 = "55c0dc1e833d4c4aa75c8f16265ae894";//天风默认除公司（除公司报告）
	public static String SENDGROUP_4 = "4c892b4e38c24e6aa6b1cccabf9462fb";//天风默认固定收益（只发送固定收益报告）
	public static String SENDGROUP_5 = "cb68715108904569adbe3a5d68b3afac";//天风默认宏观策略（宏观、策略报告）
	public static String SENDGROUP_6 = "329d38d0e8834a4c81366ce7c3ac2cd6";//天风默认全部报告-研究所定制（全部报告）
	public static String SENDGROUP_7 = "6116665b07744f099ecb99b4c2d9130b";//资讯终端（Wind）
	public static String SENDGROUP_8 = "4a56e7994ec74f4eafe43bee720bbe5e";//天风默认基金（全部报告）
	public static String SENDGROUP_9 = "fffb942f512349ea8db6ce303c54abbf";//富国基金
	public static String SENDGROUP_10 = "cef0918a4f384d6eaf48ec2ab4a2ca73";//天风默认公司行业（公司、行业报告）
	public static String SENDGROUP_11 = "3b338a43743243fc91dfec7d6f3bcd23";//易方达
	public static String SENDGROUP_12 = "4028abd7565365fa0156581d82610823";//资讯终端（同花顺）
	public static String SENDGROUP_13 = "297e62b357f693780157f6d7fcc706e5";//富国终端接收报告
	public static String SENDGROUP_14 = "297e62b35f910182015f946599cd0cae";//天风默认深度报告
	public static String SENDGROUP_15 = "297e62b35be6c2bc015bf751fd3549e9";//天风默认金融工程（只发送金融工程）
	public static String SENDGROUP_16 = "297e62b35be6c2bc015bf75481a04a26";//天风默认除海外（只发送海外报告）
	public static String SENDGROUP_17 = "297e62b35e56e92c015e73cf319e10d9";//天风默认_港股报告
	public static String SENDGROUP_18 = "297e62b35df57938015e312dc0f03fe3";//标准基金公司发送组


	
	
}
