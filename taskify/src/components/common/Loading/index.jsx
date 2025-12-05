import React from 'react';
import classNames from 'classnames/bind';
import styles from './Loading.module.scss';

const cx = classNames.bind(styles);

/**
 * @component Loading
 * @description A simple loading spinner component.
 * @returns {JSX.Element} The Loading component.
 */
function Loading() {
  return (
    <div className={cx('wrapper')}>
      <div className={cx('spinner')}></div>
    </div>
  );
}

export default Loading;
