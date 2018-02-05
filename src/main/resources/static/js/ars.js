$(document).ready(function(){

    $('#neTypeSelect').change(function(e) {
        $this = $(e.target);
        var url = window.location.href;
        var baseUrl = url.substr(0, url.indexOf("?"));
        window.location.href = baseUrl + "?neTypeId=" + $this.val();
    });

    $('arsCreateBtn').click(function(e) {
        $this = $(e.target);

        $.ajax({
            type:'GET',
            beforeSend: function(){
                $('.ajax-loader').css("visibility", "visible");
            },
            url:'/quantityPlus',
            data: {
                'productId':p1,
                'quantity':p2,
                'productPrice':p3},
            success:function(data){
                $('#'+p1+'value').text(data.newProductQuantity);
                $('#'+p1+'amount').text("₹ "+data.productAmount);
                $('#totalUnits').text(data.newNoOfUnits);
                $('#totalAmount').text("₹ "+data.newTotalAmount);
            },
            complete: function(){
                $('.ajax-loader').css("visibility", "hidden");
            }
        });
    });
});