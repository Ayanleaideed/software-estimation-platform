document.addEventListener('DOMContentLoaded', function () {
    const datePickerElement = document.getElementById('estEndDatePicker');
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
            const estEndDate = datePickerInput.getAttribute('value');
            datePicker.updateOptions({ defaultDate: new Date(estEndDate) });
            datePicker.setValue(new Date(estEndDate));
        }

    } else {
        console.error('Date picker element not found.');
    }
});
