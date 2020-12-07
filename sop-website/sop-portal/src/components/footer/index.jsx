import React from 'react';
import PropTypes from 'prop-types';
// import siteConfig from '../../../site_config/site';
// import { getLink } from '../../../utils';
import './index.scss';
import { getLink } from '../../../utils';

const propTypes = {
  logo: PropTypes.string.isRequired, // logo地址
  language: PropTypes.oneOf(['zh-cn', 'en-us']),
};

const beianStyle = {
    div: {
        margin: '0 auto',
        padding: '20px 0',
        a: {
            color: '#999',
            paddingRight: '10px',
            textDecoration: 'none',
            height: '20px',
            lineHeight: '20px',
            img: {
                float: 'left'
            },
            p: {
                float: 'left',
                height: '20px',
                lineHeight: '20px',
                margin: '0px 0px 0px 5px',
                color: '#939393'
            }
        }
    }
}

class Footer extends React.Component {

  render() {
    // const { logo, language } = this.props;
    // const dataSource = siteConfig[language];
    return (
      <footer className="footer-container">
        <div className="footer-body">
          <div className="copyright">
              <span>
                  <div style={beianStyle.div}>
                    <a style={beianStyle.div.a}>联系我们：xxx@163.com</a>
                  </div>
              </span>
          </div>
        </div>
      </footer>
    );
  }
}

Footer.propTypes = propTypes;

export default Footer;
