$(document).ready(function () {
    bindListener();

   $("#adaptAddBtn").click(function () {
       $("#adaptDiv").append('<div class="row form-group">\n' +
           '                     <div class="col-sm-9">\n' +
           '                        <input class="form-control" name="adaptations" placeholder="Adaptation ID" />\n' +
           '                     </div>\n' +
           '                     <div class="col-sm-3">\n' +
           '                        <button name="removeBtn" type="button" class="btn btn-danger"><span class="glyphicon glyphicon-minus"></span></button>\n' +
           '                     </div>\n' +
           '                  </div>');
       bindListener();
   });


    function bindListener() {
        $("button[name=removeBtn]").unbind().click(function(){
            $(this).parent().parent().remove();
        })
    };
});