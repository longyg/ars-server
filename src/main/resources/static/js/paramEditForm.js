$(document).ready(function() {
   $("#saveAsNewBtn").click(function() {
      $("#paramEditForm").attr("action", "/param/save");
   });
});