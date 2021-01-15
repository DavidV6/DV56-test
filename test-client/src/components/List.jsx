import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';

const StyledList = styled.ul`
  margin: 0;
  padding: 10px;
  border: 1px solid #ccc;
  overflow: auto;
`;

const StyledListItem = styled.li`
  list-style: none;
`;

const List = ({
  className,
  data,
  onClick
}) => (
  <StyledList className={className}>
    {data.map(item => (
      <StyledListItem
        key={item.key}
        onClick={() => {
          if (item.props.onClick) item.props.onClick();
          onClick(item);
        }}
      >
        {item}
      </StyledListItem>
    ))}
  </StyledList>
);

List.defaultProps = {
  onClick: () => null
};

List.propTypes = {
  className: PropTypes.string.isRequired,
  data: PropTypes.array.isRequired,
  onClick: PropTypes.func
};

export default List;
