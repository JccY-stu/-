$(function(){
    //计算总价
    var array = $(".qprice");
    var totalCost = 0;
    for(var i = 0;i < array.length;i++){
        var val = parseInt($(".qprice").eq(i).html().substring(1));
        totalCost += val;
    }
    $("#totalprice").html("￥"+totalCost);
    //settlement2使用
    $("#settlement2_totalCost").val(totalCost);
});

//商品数量++
function addQuantity(obj){
    let index = $(".car_btn_2").index(obj);
    let quantity = parseInt($(".car_ipt").eq(index).val());
    let stock = parseInt($(".productStock").eq(index).val());
    if(quantity == stock){
        alert("库存不足！");
        return false;
    }
    quantity++;
    let price = parseFloat($(".productPrice").eq(index).val());
    let cost = quantity * price;
    let id = parseInt($(".id").eq(index).val());
    //将最新的quantity cost发给后台，动态更新数据库
    //基于jQuery的Ajax
    //基于js的Ajax
    //基于Vue的axios
    $.ajax({
        url:"/cart/update/"+id+"/"+quantity+"/"+cost,
        type:"POST",
        success:function (data) {
            if(data == "success"){
                $(".qprice").eq(index).text('￥'+cost);
                $(".car_ipt").eq(index).val(quantity);
                let array = $(".qprice");
                let totalCost = 0;
                for(let i = 0;i < array.length;i++){
                    let val = parseInt($(".qprice").eq(i).html().substring(1));
                    totalCost += val;
                }
                $("#totalprice").html("￥"+totalCost);
            }
        }
    });

}

//商品数量--
function subQuantity(obj) {
    let index = $(".car_btn_1").index(obj);
    let quantity = parseInt($(".car_ipt").eq(index).val());
    if(quantity == 1){
        alert("至少商品数量为1！");
        return false;
    }
    quantity--;
    let price = parseFloat($(".productPrice").eq(index).val());
    let cost = quantity * price;
    $(".qprice").eq(index).text('￥'+cost);
    $(".car_ipt").eq(index).val(quantity);
}

// //移出购物车
// function removeCart(obj){
//     var index = $(".delete").index(obj);
//     var id = $(".id").eq(index).val();
//     if(confirm("是否确定要删除？")){
//         window.location.href = "/product/removeCart/"+id;
//     }
// }

//移出购物车
function removeCart(obj){
    let index = $(".delete").index(obj);
    let id = parseInt($(".id").eq(index).val());
    alert(id);
    if(confirm("是否确定删除?")){
        window.location.href = "/cart/deleteById/"+id;
    }
}

//删除订单
function removeOrders(){
    var deleteOrderDetailId = document.getElementById("OrderDetailId");
    // alert(deleteOrderDetailId.value);
    if(confirm("是否确定删除该订单?")){
        window.location.href = "/orders/deleteById/"+(deleteOrderDetailId.value);
    }
}

//删除地址
function removeAddress() {
    var deleteAddressId = document.getElementById("addressId");
    // alert(deleteAddressId.value);
    if(confirm("是否确定删除该地址?")){
        window.location.href = "/userAddress/deleteById/"+(deleteAddressId.value);
    }
}

function settlement2() {
    var totalCost = $("#totalprice").text();
    if(totalCost == "￥0"){
        alert("购物车为空，不能结算！");
        return false;
    }
    window.location.href="/cart/settlement2";
}

//确认订单，减少库存，进入支付页面
function settlement3() {
    //提交表单
    $("#form").submit();
}
