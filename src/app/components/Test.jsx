import React from 'react';
import { Checkbox } from 'flowbite-react';

const Test = () => {
  const colors = [
    { id: 'red-checkbox', label: 'Red', textColor: 'text-red-600', borderColor: 'border-red-600', focusRing: 'focus:ring-red-500 dark:focus:ring-red-600' },
    { id: 'green-checkbox', label: 'Green', textColor: 'text-green-600', borderColor: 'border-green-600', focusRing: 'focus:ring-green-500 dark:focus:ring-green-600' },
    { id: 'purple-checkbox', label: 'Purple', textColor: 'text-purple-600', borderColor: 'border-purple-600', focusRing: 'focus:ring-purple-500 dark:focus:ring-purple-600' },
    { id: 'teal-checkbox', label: 'Teal', textColor: 'text-teal-600', borderColor: 'border-teal-600', focusRing: 'focus:ring-teal-500 dark:focus:ring-teal-600' },
    { id: 'yellow-checkbox', label: 'Yellow', textColor: 'text-yellow-400', borderColor: 'border-yellow-400', focusRing: 'focus:ring-yellow-500 dark:focus:ring-yellow-600' },
    { id: 'orange-checkbox', label: 'Orange', textColor: 'text-orange-500', borderColor: 'border-orange-500', focusRing: 'focus:ring-orange-500 dark:focus:ring-orange-600' },
  ];

  return (
    <div className="flex flex-wrap">
      {colors.map((color) => (
        <div key={color.id} className="flex items-center me-4">
          <Checkbox
            id={color.id}
            value=""
            defaultChecked
            className={`w-4 h-4 ${color.textColor} bg-gray-100 ${color.borderColor} border-2 rounded ${color.focusRing} dark:ring-offset-gray-800 dark:bg-gray-700 dark:border-gray-600`}
          />
          <label htmlFor={color.id} className="ms-2 text-sm font-medium text-gray-900 dark:text-gray-300">
            {color.label}
          </label>
        </div>
      ))}
    </div>
  );
};

export default Test;
