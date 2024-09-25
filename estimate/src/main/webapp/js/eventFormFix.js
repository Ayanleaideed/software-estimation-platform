document.addEventListener('DOMContentLoaded', function () {
    const formElement = document.getElementById('dateForm');
    const selectElement = document.getElementById('category');
    const hiddenCategoryElement = document.getElementById('hiddenCategory');

    if(formElement && selectElement && hiddenCategoryElement){
        formElement.addEventListener('submit', function() {
            hiddenCategoryElement.value = selectElement.value;
        });
    }
});
document.addEventListener('DOMContentLoaded', function () {
    const formElement = document.getElementById('dateForm');
    const selectElement = document.getElementById('category');
    const hiddenCategoryElement = document.getElementById('hiddenCategory');

    if (formElement) {
        formElement.addEventListener('submit', function () {
            hiddenCategoryElement.value = selectElement.value;
            selectElement.disabled = true;
        });
    }
});
