<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>View Parameters</title>
</head>
<body>

<div class="container-fluid">
    <#include "/inc/nav.ftl" />

    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
            <div><h3>Parameters of ${neParam.neType} ${neParam.neVersion} (v${neParam.v})</h3></div>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Parameter Name</th>
                    <th>Parameter Value</th>
                </tr>
                </thead>
                <tbody>
                <#list neParam.variables as variable>
                    <tr>
                        <td>${variable.name}</td>
                        <td>${variable.value}</td>
                    </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <div class="col-md-1"></div>
    </div>

    <#include "/inc/footer.ftl" />
</div>

<script src="/js/jquery-1.9.1.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
</body>
</html>