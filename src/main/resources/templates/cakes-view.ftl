
<html>

<body>

<table>
 <#list cakes as cake>
    <tr><td>${cake.title}</td></tr>
    <tr><td>${cake.desc}</td></tr>
    <tr><td>${cake.ingredients?join(", ")}</td></tr>
    <tr><td>${cake.price}</td></tr>
    <tr><td><img src="${cake.image}"></td></tr>
    

 </#list>
</table>

</body>
</html>