// utils/message.js
import Message from 'tdesign-miniprogram/message/index';

const defaultOptions = {
  duration: 5000,
  offset: [20, 32]
};

const showMessage = (type, options) => {
  Message[type]({
    ...defaultOptions,
    ...options,
  });
};

const message = {
  info(content, duration) {
    showMessage('info', { content: content, duration: duration });
  },
  warning(content, duration) {
    showMessage('warning', { content: content, duration: duration });
  },
  success(content, duration) {
    showMessage('success', { content: content, duration: duration });
  },
  error(content, duration) {
    showMessage('error', { content: content, duration: duration });
  },
};

export default message;