document.addEventListener('DOMContentLoaded', function() {
    const startPickerElement = document.getElementById('startPicker');
    const endPickerElement = document.getElementById('endPicker');

    if (startPickerElement && endPickerElement) {
        const commonOptions = {
            display: {
                components: {
                    calendar: true,
                    clock: false
                }
            },
            localization: {
                format: 'MM/dd/yyyy'
            }
        };

        const startPicker = new tempusDominus.TempusDominus(startPickerElement, commonOptions);
        const endPicker = new tempusDominus.TempusDominus(endPickerElement, {
            ...commonOptions,
            useCurrent: false,
        });

        startPickerElement.addEventListener(tempusDominus.Namespace.events.change, (e) => {
            endPicker.updateOptions({
                restrictions: {
                    minDate: e.detail.date,
                },
            });
        });

        endPickerElement.addEventListener(tempusDominus.Namespace.events.change, (e) => {
            startPicker.updateOptions({
                restrictions: {
                    maxDate: e.detail.date,
                },
            });
        });
    } else {
        console.error('Date picker element not found.');
    }
});
