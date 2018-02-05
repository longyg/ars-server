<!DOCTYPE html>
<#import "/spring.ftl" as spring />
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/ars.css" rel="stylesheet">
    <title>Alarm</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
            font-size: 8pt;
        }
        table, th, td {
            border: 1px solid black;
        }
        th {
            background-color: #c1e2b3;
            font-weight: bold;
        }
        th, td {
            height: 18px;
            line-height: 15px;
            padding: 0px 5px 0px 5px;
            text-align: left;
        }
        td {
            white-space: nowrap;
        }
    </style>
</head>
<body>

<div class="container-fluid">
    <#include "/inc/nav.ftl" />

    <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-12">
            <#if spec??>
            <div><h3>Alarm of ${spec.neType} ${spec.neVersion}</h3></div>
            <#list neType.adaptSet as adap>
                <#if spec.alarmMap[adap]??>
                <div><h3>Adaptation ID: ${adap}</h3></div>
                <div style="overflow: auto;">
                <table>
                    <thead>
                    <tr>
                        <th>Specific Problem</th>
                        <th>Is supported by ${spec.neVersion}</th>
                        <th>Supported previous versions</th>
                        <th>Alarm Text</th>
                        <th>Probable Cause</th>
                        <th>Alarm Type</th>
                        <th>Severity</th>
                        <th>Meaning</th>
                        <th>Instructions</th>
                        <th>Cancelling</th>
                    </tr>
                    </thead>
                    <tbody>
                        <#list spec.alarmMap[adap] as alarm>
                            <tr>
                                <td>
                                    ${alarm.specificProblem}
                                </td>
                                <td>
                                    ${alarm.supported?string('Yes', 'No')}
                                </td>
                                <td>
                                    ${alarm.supportedOtherReleases}
                                </td>
                                <td>
                                    ${alarm.alarmText}
                                </td>
                                <td>
                                    ${alarm.probableCause}
                                </td>
                                <td>
                                    ${alarm.alarmType}
                                </td>
                                <td>
                                    ${alarm.perceivedSeverityInfo}
                                </td>
                                <td>
                                    ${alarm.meaning}
                                </td>
                                <td>
                                    ${alarm.instructions}
                                </td>
                                <td>
                                    ${alarm.cancelling}
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
                </div>
                </#if>
            </#list>
            </#if>
        </div>
        <div class="col-md-1"></div>
    </div>

    <#include "/inc/footer.ftl" />
</div>

<script src="/js/jquery-1.9.1.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
<script src="/js/ars.js"></script>
</body>
</html>