$(function(){
    //给type绑定点击事件
    $(".type").click(function () {
        var index = $(".type").index(this);
        var obj = $(".type").eq(index);
        $(".type").removeClass("checked");
        obj.addClass("checked");
    });
    //给color绑定点击事件
    $(".color").click(function () {
        var index = $(".color").index(this);
        var obj = $(".color").eq(index);
        $(".color").removeClass("checked");
        obj.addClass("checked");
    });
});

function increase(){
    let quantity = $("#quantity").val();
    let stockStr = $("#stock").text();
    let stock = parseInt(stockStr);
    if(quantity < stock){
        quantity++;
    }
    $("#quantity").val(quantity);
}

function reduce() {
    let quantity = $("#quantity").val();
    if(quantity > 1){
        quantity--;
    }
    $("#quantity").val(quantity);
}

//添加购物车
function addCart(productId,storeId,price){
    let stockStr = $("#stock").text();
    let storename01 = $("#storeName").text();
    let storeName = storename01.toString();
    let stock = parseInt(stockStr);
    alert("该商品所属店铺为：" + storeId);
    alert("该商品所属店铺名称为：" + storeName);
    alert("该商品价格为：" + price);
    if(stock == 0){
        alert("库存不足！");
        return false;
    }
    let quantity = $("#quantity").val();
    window.location.href="/cart/add/"+productId+"/"+storeId+"/"+storeName+"/"+price+"/"+quantity;
}

//秒杀商品
function kill(productId){
    alert("秒杀商品 ID 为：" + productId);
    window.location.href="/stock/killtoken/"+productId;
}