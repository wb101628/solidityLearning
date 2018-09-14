<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta HTTP-EQUIV="pragma" CONTENT="no-cache">
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-store, must-revalidate">
<meta HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<meta HTTP-EQUIV="expires" CONTENT="0">
<script language="javascript" type="text/javascript"
	src="../js/batchForTest_abi.js"></script>
<script language="javascript" type="text/javascript"
	src="../js/batchTransfer_abi.js"></script>
<script language="javascript" type="text/javascript"
	src="../js/HTforMain_abi.js"></script>
<script language="javascript" type="text/javascript"
	src="../js/HTforTest_abi.js"></script>
<script language="javascript" type="text/javascript"
	src="../js/web3.min.js"></script>
<title>Insert title here</title>
</head>
<%@include file="common.jsp"%>
<body>
	<script type="text/javascript">
		if (typeof web3 !== 'undefined') {
			web3 = new Web3(web3.currentProvider);
		} else {
			// set the provider you want from Web3.providers
			web3 = new Web3(
					new Web3.providers.HttpProvider(
							"https://ropsten.infura.io/0x1783f45a37eca27ff88f388f38f5f55c7af62a63"));
		}

		web3.eth.defaultAccount = web3.eth.accounts[0];

		var account = web3.eth.defaultAccount;

		var infoContract = web3.eth.contract(BatchABI);

		var DDCContract = web3.eth.contract(HTABI);

		var multiSendAddress = '0x410804f20daf6498ec564a20132985b8da401c2e';

		var MultiSend = infoContract.at(multiSendAddress);

		var ddcArr = '0x6f259637dcD74C767781E37Bc6133cd6A68aa161';

		var ddccontract = DDCContract.at(ddcArr);
	</script>
	<div class="table-responsive">
		<form id="form_table">
			<input type="file" name="file" /> <input type="button" value="导入"
				onclick="test();" />
		</form>
		
		<!-- //pending -->
		<script type="text/javascript">
		 var myEvent = ddccontract.Approval({}, {fromBlock:'latest', toBlock:'pending' });
		    myEvent.watch(function(error, result){
				console.log(result);
				batchTransfer();
		    });
			var accounts;
			var counts;
			var tatol;
			var batchEvent = MultiSend.Multisended({}, {fromBlock:'latest', toBlock: 'pending'});
		    Multisended();
			function Multisended(){
				/* batchEvent.watch(function(error, result){
					console.log(result);
			    }); */
				ddccontract.allowance(account,multiSendAddress,
						function(error, result) {
					if (error == null) {
						console.log(result)
					}
				});
				/* batchTransfer(); */
			}
			
			function batchTransfer() {
				MultiSend.multiSend(ddcArr, accounts,counts,
				function (error, result) {
	                console.log(result)
	            });
			}
			function test() {
				var form = $('#form_table')[0];

				var data = new FormData(form);

				$.ajax({
					url : "http://localhost:8080/import",
					type : "post",
					enctype : 'multipart/form-data',
					data : data,
					contentType : false,
					processData : false,
					success : function(data) {
						console.log(data.accounts);
						accounts = data.accounts;
						counts = data.counts;
						tatol = data.total;
						approve();
					},
					error : function(e) {

					}
				});
			}
			function approve() {
				ddccontract.approve(multiSendAddress, tatol,
						function(error, result) {
							if (error == null) {
								console.log(result)
							}
						});
			}
			
		</script>
	</div>


</body>
</html>