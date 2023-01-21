function updateSelectBox() {
  let typPojisteni = $("#typPojisteni").val();
  $.get("http://localhost:8080/pojisteni/hodnoty-selectboxu/" + typPojisteni, function(options, status) {
    let optionsList = [];
    Object.keys(options).forEach(function(index) {
      optionsList.push(new Option(options[index], index));
    });
    let predmetPojisteniSelectBox = $("#predmetPojisteni");
    predmetPojisteniSelectBox.html(optionsList);
  });
}