document.addEventListener('DOMContentLoaded', function () {
    const datePickerElement = document.getElementById('eventDatePicker');
    
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
                minDate: new Date()
            },
            dateRange: false,
        };

        new tempusDominus.TempusDominus(datePickerElement, options);

    } else {
        console.error('Date picker element not found.');
    }
});