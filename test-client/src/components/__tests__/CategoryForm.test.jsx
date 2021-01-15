import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import CategoryForm from '../CategoryForm';

const onSubmit = jest.fn();
const knownGroups = ['unknown', 'known'];
const initialContent = {
  catid: 1,
  catgroup: 'unknown',
  catname: 'what',
  catdesc: 'what category am i'
}

describe('<CategoryForm />', () => {
  it('should render right category', () => {
    const { queryByTestId } = render(<CategoryForm
      initialContent={initialContent}
      knownGroups={knownGroups}
      submit={onSubmit}
    />);

    expect(queryByTestId('category-group').value).toEqual(initialContent.catgroup);
    expect(queryByTestId('category-name').value).toEqual(initialContent.catname);
    expect(queryByTestId('category-desc').value).toEqual(initialContent.catdesc);
  });


  it('should call submit event', () => {
    const { queryByTestId } = render(<CategoryForm
      initialContent={initialContent}
      knownGroups={knownGroups}
      submit={onSubmit}
    />);

    fireEvent.click(queryByTestId('submit-category-button'));
    expect(onSubmit).toHaveBeenCalled();
  });
});

