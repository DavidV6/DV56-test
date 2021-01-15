import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import App from '../App';

const backendHost = 'randon.url';
const fetchMock = jest.fn();;

describe('<App />', () => {
  it('should render the main app', () => {
    const { container } = render(<App config={{ backendHost }}/>);

    expect(container).toBeInTheDocument();
  });

  it('should fetch with correct api', () => {
    const { queryByTestId } = render(<App config={{ backendHost }} />);

    const createCategoryButton = queryByTestId('submit-category-button');
    expect(createCategoryButton).not.toBeNull();

    global.fetch = fetchMock;
    fireEvent.click(createCategoryButton);

    expect(fetchMock).toHaveBeenCalledWith(`http://${backendHost}/categories/upsert`, expect.anything());
  });
});

