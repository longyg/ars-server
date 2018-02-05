$(document).ready(function(){

    $('#neTypeSelect').change(function(e){
        $this = $(e.target);
        var url = window.location.href;
        var baseUrl = url.substr(0, url.indexOf("?"));
        window.location.href = baseUrl + "?neTypeId=" + $this.val();
    });

    $('#neVersionSelect').change(function(e){
        $this = $(e.target);
        var url = window.location.href;
        var baseUrl = url.substr(0, url.indexOf("?"));
        window.location.href = baseUrl + "?neTypeId=" + $('#neTypeSelect').val() + "&neRelId=" + $this.val();
    });
});