import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import EventForm from '../EventForm';

const onSubmit = jest.fn();
const venues = [
  {
    venueid: 1,
    venuename: 'local gym'
  }, {
    venueid: 2,
    venuename: 'your place'
  }
];
const categories = [
  {
    catid: 1,
    catname: 'unknown'
  }, {
    catid: 2,
    catname: 'cat'
  }
];

describe('<EventForm />', () => {
  it('should render event form', () => {
    const { queryByTestId } = render(<EventForm
      venues={venues}
      categories={categories}
      submit={onSubmit}
    />);

    expect(queryByTestId('event-venue')).not.toBeNull();
    expect(queryByTestId('event-category')).not.toBeNull();
    expect(queryByTestId('event-name')).not.toBeNull();
    expect(queryByTestId('event-datetime')).not.toBeNull();
    expect(queryByTestId('event-holiday')).not.toBeNull();
  });


  it('should call submit event', () => {
    const { queryByTestId } = render(<EventForm
      venues={venues}
      categories={categories}
      submit={onSubmit}
    />);

    fireEvent.click(queryByTestId('submit-event-button'));
    expect(onSubmit).toHaveBeenCalled();
  });
});

