var customer_window = new PopWindow('.customer', {
	title: '关联客户',
	template: '#customer-template'
}).init();

customer_window.bind('open', function() {
	new GridTable('#customer-grid-table', {
		dataSource: [
    {
        c1: "大有票务",
        c2: "同行",
        c3: "支付宝",
        c4: "月结",
        c5: "2013/01/02",
        c6: "张三"
    },
    {
        c1: "大有票务",
        c2: "同行",
        c3: "支付宝",
        c4: "月结",
        c5: "2013/01/02",
        c6: "张三"
    }
        
        
        ],
		rowTemplate: kendo.template($("#customer-row-template ").html())
	}).init();
});

var policy_window = new PopWindow('.policy', {
	title: '关联政策',
    width: '1000px',
	template: '#policy-template'
}).init();

policy_window.bind('open', function() {
	new GridTable('#policy-grid-table', {
        scrollable: true,
        resizable: true,
        columns: [
    {
        field: "c1",
        title: "航司",
        width: 20
    },
    {
        field: "c2",
        title: "出发机场",
        width: 30
    },
    {
        field: "c3",
        title: "到达机场",
        width: 30
    },
    {
        field: "c4",
        title: "航程",
        width: 30
    },
    {
        field: "c5",
        title: "航班限制",
        width: 30
    },
    {
        field: "c6",
        title: "适用舱位",
        width: 30
    },
    {
        field: "c6",
        title: "返点",
        width: 30
    },
    {
        field: "c7",
        title: "后返",
        width: 30
    },
    {
        field: "c8",
        title: "乘客类型",
        width: 30
    },
    {
        field: "c9",
        title: "票证",
        width: 30
    },
    {
        field: "c10",
        title: "出票",
        width: 30
    },
    {
        field: "c11",
        title: "出票日期",
        width: 30
    },
    {
        field: "c12",
        title: "乘机日期",
        width: 30
    },
    {
        field: "c13",
        title: "发布日期",
        width: 30
    },
    {
        field: "c14",
        title: "发布人",
        width: 30
    },
    {
        field: "c15",
        title: "状态",
        width: 30
    },
    {
        field: "c16",
        title: "标签名称",
        width:30
    }

        ],
		dataSource: [
    {
        c1: "MU",
        c2: "SHA/PEK",
        c3: "PEX",
        c4: "单程",
        c5: "所有",
        c6: "ABCDEF",
        c7: 6.0,
        c8: 2.0,
        c9: "成人",
        c10: "BSP",
        c11: "自动",
        c12: "2013/09/01至2013/09/02",
        c13: "2013/09/01至2013/09/02",
        c14: "2013/09/01至2013/09/02",
        c15: "zhangjian",
        c16: "已发布",
        c17: "东方航空\\南方航空",
    }
        
        
        ]
		//rowTemplate: kendo.template($("#policy-row-template ").html())
	}).init();
});

