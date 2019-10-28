package com.zero.library.widget.snakebar;

import com.ubzx.library.R;

public enum Prompt {
    /**
     * 红色,错误
     */
    ERROR(R.drawable.ic_cancel_24dp, R.color.prompt_error),//prompt_error  ic_cancel_24dp

    /**
     * 红色,警告
     */
    WARNING(R.drawable.ic_error_24dp, R.color.prompt_warning),//prompt_warning),

    /**
     * 绿色,成功
     */
    SUCCESS(R.drawable.ic_check_circle_24dp, R.color.prompt_success);//prompt_success);

    private int resIcon;
    private int backgroundColor;

    Prompt(int resIcon, int backgroundColor) {
        this.resIcon = resIcon;
        this.backgroundColor = backgroundColor;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
