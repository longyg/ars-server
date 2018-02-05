<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>Adaptation Resource</title>
</head>
<body>

<div class="container-fluid">
<#include "/inc/nav.ftl" />

    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-10">
            <div><h3>Adaptation Resource</h3></div>
            <div style="margin:10px">
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createResourceFromModal">
                    <span class="glyphicon glyphicon-plus-sign"></span> Add
                </button>
            </div>
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>Adaptation ID</th>
                    <th>Adaptation Release</th>
                    <th>SVN Root</th>
                    <th>Resource Path</th>
                    <th>Operations</th>
                </tr>
                </thead>
                <tbody>
                <#list adaptationResources as src>
                <tr>
                    <td>${src.adaptation.id}</td>
                    <td>${src.adaptation.release}</td>
                    <td>${src.svnRoot}</td>
                    <td>${src.sourcePath}</td>
                    <td>
                        <a class="btn btn-sm btn-danger" href="/resource/delete?id=${src.id}">
                            <span class="glyphicon glyphicon-remove-sign"></span> Delete
                        </a>
                    </td>
                </tr>
                </#list>
                </tbody>
            </table>
        </div>
        <div class="col-md-1"></div>
    </div>

<#include "/inc/footer.ftl" />
</div>

<div class="modal fade" id="createResourceFromModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <!-- Modal Header -->
            <div class="modal-header">
                <button type="button" class="close"
                        data-dismiss="modal">
                    <span aria-hidden="true">&times;</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    Add Adaptation Resource
                </h4>
            </div>

            <!-- Modal Body -->
            <div class="modal-body">
                <form class="form-horizontal" role="form" action="/resource/add" method="post">
                    <div class="form-group">
                        <label  class="col-sm-4 control-label" for="adaptationId">Adaptation ID</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="adaptationId" name="adaptation.id" placeholder="Adaptation ID"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" for="adaptationRelease">Adaptation Release</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="adaptationRelease" name="adaptation.release" placeholder="Adaptation Release"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" for="svnRoot" >SVN Root URL</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="svnRoot" name="svnRoot" placeholder="SVN Root URL"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" for="svnUser" >SVN User</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="svnUser" name="svnUser" placeholder="SVN User"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" for="svnPassword" >SVN Password</label>
                        <div class="col-sm-8">
                            <input class="form-control" type="password" id="svnPassword" name="svnPassword" placeholder="SVN Password"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-4 control-label" for="sourcePath" >Resource Path</label>
                        <div class="col-sm-8">
                            <input class="form-control" id="sourcePath" name="sourcePath" placeholder="Resource Path"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <div class="col-sm-offset-8 col-sm-4">
                            <button type="submit" class="btn btn-primary">Save</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="/js/jquery-1.9.1.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
</body>
</html>