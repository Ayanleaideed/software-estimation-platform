document.addEventListener('DOMContentLoaded', function () {
    var slider = document.getElementById('resultSlider');
    var output = document.getElementById('resultValue');

    output.textContent = slider.value;

    slider.oninput = function() {
        output.textContent = this.value;
    };
});
