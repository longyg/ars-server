<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>ARS</title>
</head>
<body>

<div class="container-fluid">
<#include "/inc/nav.ftl" />

    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
            <div><h2>ARS for ${ne.neType} ${ne.neVersion}</h2></div>
            <div style="margin:10px 0 10px 0">
                <a class="btn btn-primary" href="/ars/edit?neType=${ne.neType}&neVersion=${ne.neVersion}">
                    <span class="glyphicon glyphicon-edit"></span> Edit
                </a>
                <a class="btn btn-primary" href="/ars/export?neType=${ne.neType}&neVersion=${ne.neVersion}">
                    <span class="glyphicon glyphicon-export"></span> Export
                </a>
            </div>
            <#if usSpec??>
                <div>
                    <h3>ARS Title</h3>
                    <p></p>${usSpec.title.value}</p>
                </div>
                <div>
                    <h3>Adaptation Information</h3>
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Value</th>
                        </tr>
                        </thead>
                        <tbody>
                            <#list usSpec.adapInfos as adapInfo>
                            <tr>
                                <td>${adapInfo.name}</td>
                                <td>${adapInfo.htmlValue}</td>
                            </tr>
                            </#list>
                        </tbody>
                    </table>
                </div>
                <div>
                    <h3>User Stories</h3>
                    <#list usSpec.userStories as userstory>
                        <h4>${userstory.htmlValue}</h4>
                        <table class="table table-bordered" style="table-layout: fixed">
                            <thead>
                            <tr>
                                <th width="35px">Selected</th>
                                <th width="200px">Name</th>
                                <th width="260px">Description</th>
                                <th width="100px">Rationale</th>
                                <th width="100px">Open Issue</th>
                            </tr>
                            </thead>
                            <tbody>
                                <#list userstory.subTasks as subTask>
                                <tr>
                                    <td style="text-align: center;vertical-align: middle">
                                        <#if subTask.select.value == "true">
                                            <span class="glyphicon glyphicon-ok" style="color:green"></span>
                                        <#else>
                                            <span class="glyphicon glyphicon-remove" style="color:red"></span>
                                        </#if>
                                    </td>
                                    <td>
                                        <#if subTask.name??>
                                            ${subTask.name.htmlValue}
                                        </#if>
                                    </td>
                                    <td>
                                        <#if subTask.description??>
                                            ${subTask.description.htmlValue}
                                        </#if>
                                    </td>
                                    <td>
                                        <#if subTask.rationale??>
                                            ${subTask.rationale.htmlValue}
                                        </#if>
                                    </td>
                                    <td>
                                        <#if subTask.issue??>
                                            ${subTask.issue.htmlValue}
                                        </#if>
                                        </td>
                                    </tr>
                                    </#list>
                                </tbody>
                            </table>
                        </#list>
                    </div>
                <#else>
                    <p>You have not create ARS for ${ne.neType} ${ne.neVersion}</p>
                    <a class="btn btn-primary" href="/ars/create?neType=${ne.neType}&neVersion=${ne.neVersion}">Click to create</a>
                </#if>
        </div>
        <div class="col-md-1"></div>
    </div>

<#include "/inc/footer.ftl" />
</div>

<script src="/js/jquery-1.9.1.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
</body>
</html>