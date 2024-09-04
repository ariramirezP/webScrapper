

let aux = document.location.href.split("?query=");
let query = aux[1];
document.getElementById("txtInput").value = query;

fetch('http://localhost:8080/api/search?query=' + query,)
    .then(response => response.json())
    .then(json =>{
        let html = '';
        for(let result of json){
         html+= createHtmlElements(result);
        }
        document.getElementById("mainContent").outerHTML = html;
    });

function createHtmlElements(result){
    return`<section>
             <h2><a href="${result.url}" target="_blank">${result.title}</a></h2>
             <p>${result.description}</p>
          </section> `;
}

function search(){
    let textSearch = document.getElementById("txtInput").value;
    if(textSearch != null){
         document.location.href = "results.html?query=" + textSearch;
    }
    else{
        alert("please insert a search term!!!!")
    }

}