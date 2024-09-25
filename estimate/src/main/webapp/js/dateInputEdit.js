document.addEventListener('DOMContentLoaded', function () {
    const datePickerElement = document.getElementById('eventDatePicker');
    const datePickerInput = datePickerElement.querySelector('.datetimepicker-input');

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
            dateRange: false,
            useCurrent: false
        };

        const datePicker = new tempusDominus.TempusDominus(datePickerElement, options);

        if (datePickerInput.getAttribute('value')) {
            const eventDate = datePickerInput.getAttribute('value');
            datePicker.updateOptions({ defaultDate: new Date(eventDate) });
            datePicker.setValue(new Date(eventDate));
        }

    } else {
        console.error('Date picker element not found.');
    }
});
