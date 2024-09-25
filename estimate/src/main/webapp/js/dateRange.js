document.addEventListener('DOMContentLoaded', function () {
    const datePickerElement = document.getElementById('dateRange');
    
    if (datePickerElement) {
        const options = {
            display: {
                components: {
                    calendar: true,
                    clock: false
                }
            },
            localization: {
                format: 'MM/dd/yyyy'
            },
            restrictions: {
            },
            dateRange: true,
            multipleDatesSeparator: ' - '
        };

        new tempusDominus.TempusDominus(datePickerElement, options);

    } else {
        console.error('Date picker element not found.');
    }
});

function resetDateRange() {
    document.getElementById('dateRange').value = '';
    document.getElementById('dateForm').submit();
}
