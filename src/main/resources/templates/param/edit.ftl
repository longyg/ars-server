<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>Parameters</title>
</head>
<body>

<div class="container-fluid">
    <#include "/inc/nav.ftl" />

    <div class="row">
        <div class="col-md-3"></div>
        <div class="col-md-6">
            <div><h3>Edit parameters</h3></div>

            <form class="form-horizontal" id="paramEditForm" role="form" action="/param/update" method="post">
                <input type="hidden" name="id" value="${id}" />
                <input type="hidden" name="neTypeId" value="${neTypeId}" />
                <input type="hidden" name="neRelId" value="${neRelId}" />
                <div class="form-group">
                    <label  class="col-sm-4 control-label" for="neType">NE Type</label>
                    <div class="col-sm-8">
                        <input class="form-control" id="neType" name="neType" placeholder="${neParam.neType}" value="${neParam.neType}" readonly unselectable="on"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-4 control-label" for="neVersion" >NE Version</label>
                    <div class="col-sm-8">
                        <input class="form-control" id="neVersion" name="neVersion" placeholder="${neParam.neVersion}" value="${neParam.neVersion}" readonly unselectable="on"/>
                    </div>
                </div>
                <#list neParam.variables as variable>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" for="${variable.name}" >${variable.name}</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="${variable.name}" name="${variable.name}" value="${variable.value}" />
                        </div>
                    </div>
                </#list>

                <div class="form-group">
                    <div class="col-sm-offset-4 col-sm-8">
                        <button type="submit" class="btn btn-primary">Save</button>
                        <button type="submit" id="saveAsNewBtn" class="btn btn-primary">Save As New</button>
                    </div>
                </div>
            </form>

        </div>
        <div class="col-md-3"></div>
    </div>

    <#include "/inc/footer.ftl" />
</div>

<script src="/js/jquery-1.9.1.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script src="/js/paramEditForm.js"></script>
</body>
</html>